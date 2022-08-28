package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


    @Query(value = "select * from category where keys = :prefix", nativeQuery = true)
    Category findCatogeryByPrefix(String prefix);

    @Query(value = "select ct from Category ct where lower(ct.categoryName)  = :categoryName", nativeQuery = false)
    Category findCategoryByName(String categoryName);
}
