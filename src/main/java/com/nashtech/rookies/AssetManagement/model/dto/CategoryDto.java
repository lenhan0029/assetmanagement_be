package com.nashtech.rookies.AssetManagement.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CategoryDto {
	@Pattern(regexp = "\\d*", message = "Category id must be a number")
	private Integer categoryId;
	@NotEmpty(message = "Category name must not be empty")
	private String categoryName;
	@NotEmpty(message = "key must not be empty")
	private String key;

	public CategoryDto() {}

	public CategoryDto(Integer categoryId, String categoryName, String key) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.key = key;
	}
}
