package com.asr.blogapp.service;

import com.asr.blogapp.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto  saveCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();
}
