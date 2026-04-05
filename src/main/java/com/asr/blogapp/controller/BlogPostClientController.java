package com.asr.blogapp.controller;


import com.asr.blogapp.dto.CategoryDto;
import com.asr.blogapp.dto.CommentDto;
import com.asr.blogapp.dto.PostDto;
import com.asr.blogapp.service.CategoryService;
import com.asr.blogapp.service.CommentService;
import com.asr.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;




@Controller
public class BlogPostClientController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String showAllPosts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            Model model) {
        int size = 6;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());

        Page<PostDto> postDtoPage;

        if (search != null && !search.trim().isEmpty()) {
            search = search.replaceAll("[^a-zA-Z0-9\\s]", "");
            postDtoPage = postService.searchBlogPost(search, pageable);
        }
        else if (categoryId != null) {
            postDtoPage = postService.getPostByCategory(categoryId, pageable);
        }
        else {
            postDtoPage = postService.getPostByUser(pageable);
        }

        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        model.addAttribute("postPages", postDtoPage);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("search", search);
        model.addAttribute("selectedCategoryId", categoryId);

        return "blog/publicpost";
    }

    @GetMapping("/category/{id}")
    public String getPostByCategoryId(@PathVariable Integer id,
                                      @RequestParam(defaultValue = "0") int pageNo, Model model) {

        int size = 6;

        if (pageNo < 0) pageNo = 0;

        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());

        Page<PostDto> postDtoPage = postService.getPostByCategory(id, pageable);

        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        model.addAttribute("postPages", postDtoPage);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("selectedCategoryId", id);
        model.addAttribute("currentpage", pageNo);

        return "blog/publicpost";
    }

    // search post
    @GetMapping("/searching")
    public String searchBlogPost(@RequestParam("search") String search,
                                 @RequestParam(defaultValue = "0") int pageNo , Model model) {

        int size = 6;

        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());

        search = search.replaceAll("[^a-zA-Z0-9\\s]", "");

        if (search != null && !search.isEmpty()) {

            // fecth data for db
            Page<PostDto> postDtoSearch = postService.searchBlogPost(search, pageable);

            List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

            model.addAttribute("postPages", postDtoSearch);
            model.addAttribute("categories", categoryDtoList);
            model.addAttribute("search", search);
            model.addAttribute("selectedCategoryId", null);
            model.addAttribute("currentpage", pageNo);

        } else {
            return "redirect:/";
        }

        return "blog/publicpost";
    }

    @GetMapping("/blogfullview/{id}/{url}")
    public String fullViewPost(@PathVariable("id") int id, @PathVariable("url") String url, Model model) {

       PostDto postDt = postService.getFullViewPost(id, url);

       model.addAttribute("post" , postDt);

       // comment obj dto ----
       CommentDto commentDto = new CommentDto();
       commentDto.setPostId(id);
       model.addAttribute("comment", commentDto);
       // get comments by post id
        List<CommentDto> comments = commentService.getCommentsByPostId(id);
        List<CommentDto> commentsTop = commentService.getTop5CommentsByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("comments", commentsTop);

        return "blog/fullview";
    }
}