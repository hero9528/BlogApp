package com.asr.blogapp.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

	private int id;


	@NotNull(message = "Category is required")
	private  Long categoryId;

	private CategoryDto category;

	@NotEmpty(message = "Title is required")
	@Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
	private String title;

	@NotEmpty(message = "Content is required")
	private String content;

	@NotEmpty(message = "please enter short description")
	private String shortDescription;

	private String url;
	private String status;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	private String postImageName;

}