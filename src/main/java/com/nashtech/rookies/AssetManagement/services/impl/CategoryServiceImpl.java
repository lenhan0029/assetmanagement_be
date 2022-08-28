package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceAlreadyExistException;
import com.nashtech.rookies.AssetManagement.model.dto.CategoryDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Category;
import com.nashtech.rookies.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.AssetManagement.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getListCategory() {
        List<Category> listAllCategory = this.categoryRepository.findAll();

        List<CategoryDto> listCategoryDTO = listAllCategory.stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

        return listCategoryDTO;


    }

    @Override
    public ResponseEntity<?> createNewCategory(Category category) {


        category.setCategoryId(0);
        category.setAssets(null);
        category.setCategoryName(category.getCategoryName().replaceAll("\\s+", " "));
        category.setKey(category.getKey().toUpperCase().replaceAll("\\s+", ""));

        Category categoryCheckPrefix = this.categoryRepository.findCatogeryByPrefix(category.getKey());

        if (categoryCheckPrefix == null)
        {
            String checkName = category.getCategoryName().toLowerCase();
            Category categoryCheckName = this.categoryRepository.findCategoryByName(checkName);

            if(categoryCheckName == null)
            {
                Category newCategory = this.categoryRepository.save(category);
                return ResponseEntity.ok(modelMapper.map(newCategory, CategoryDto.class));
            }
            throw new ResourceAlreadyExistException("Category is already existed. Please enter a different category");
        }
        throw new ResourceAlreadyExistException("Prefix is already existed. Please enter a different prefix ");
    }
    @Override
    public ResponseEntity<?> deleteCategory(int id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
