package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.CategoryDto;
import com.nashtech.rookies.AssetManagement.model.entities.Category;
import com.nashtech.rookies.AssetManagement.services.AdminService;
import com.nashtech.rookies.AssetManagement.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "Create New Category ",
            description = "If the response status code is 200 -> New category created successfully"

    )
    @PostMapping
    public ResponseEntity<?> createNewCategory(@RequestBody Category category){
        return this.categoryService.createNewCategory(category);
    }

    @Operation(summary = "Get List Category ",
            description = "If the response status code is 200 -> You got the Category List"
    )
    @GetMapping
    public List<CategoryDto> getListCategory(){
        return this.categoryService.getListCategory();
    }

    @Operation(summary = "Delete Category ",
            description = "Provide your id Category when delete"
    )
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteCate(@PathVariable int id){
        return this.categoryService.deleteCategory(id);
    }
}
