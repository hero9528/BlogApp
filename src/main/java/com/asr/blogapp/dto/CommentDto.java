package com.asr.blogapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Comment message is required")
    @Size(min = 4, max = 100, message = "Comment message must be between 4 and 100 characters")
    private String commentMsg;

    private LocalDateTime createdOn;

    private int postId;

    private String url;

    private String postTitle;

}
