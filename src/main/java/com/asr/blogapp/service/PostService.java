package com.asr.blogapp.service;

import com.asr.blogapp.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostDto savePost(PostDto postDto);

    Page<PostDto> getPostForCurrentUser(int pageNo, int size);

    void approvePost(int postId, String status);

    void rejectPost(int postId, String status);

    PostDto getPostById(int postId);

    void deletePost(int postId);

    void updatePost(PostDto postDto);

    // get all approved posts
    Page<PostDto> getPostByUser(Pageable pageable);

    Page<PostDto> getPostByCategory(int categoryId, Pageable pageable);

    // search for blogPoat Method

    Page<PostDto> searchBlogPost(String search, Pageable pageable);

    // All view post method

    PostDto getFullViewPost(int id, String url);
}