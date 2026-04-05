package com.asr.blogapp.serviceImpl;

import com.asr.blogapp.dto.CategoryDto;
import com.asr.blogapp.entity.CategoryEntity;
import com.asr.blogapp.repositry.CategoryRepositry;
import com.asr.blogapp.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepositry  categoryRepositry;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        if(categoryRepositry.existsByNameIgnoreCase(categoryDto.getName())){
            throw new RuntimeException("Category name already exists");
        }
        CategoryEntity categoryEntity = modelMapper.map(categoryDto, CategoryEntity.class);
        CategoryEntity savedCategory = categoryRepositry.save(categoryEntity);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepositry.findAll();

        List<CategoryDto> collectDtoList = categoryEntities.stream().map(c -> modelMapper.map(c, CategoryDto.class)).collect(Collectors.toList());

        return collectDtoList;
    }
}
