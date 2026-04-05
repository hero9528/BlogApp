package com.asr.blogapp.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private Long id;

    @NotEmpty(message = "Category name is required")
    private String name;
}
