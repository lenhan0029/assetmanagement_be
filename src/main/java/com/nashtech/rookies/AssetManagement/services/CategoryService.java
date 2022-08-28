package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.CategoryDto;
import com.nashtech.rookies.AssetManagement.model.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getListCategory();

    public ResponseEntity<?> createNewCategory(Category category);
    public ResponseEntity<?> deleteCategory(int id);
}
