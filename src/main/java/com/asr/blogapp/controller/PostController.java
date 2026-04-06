package com.asr.blogapp.controller;

import com.asr.blogapp.dto.CategoryDto;
import com.asr.blogapp.dto.CommentDto;
import com.asr.blogapp.helper.Helper;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asr.blogapp.dto.PostDto;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/blogdashborad")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/allPost")
    public String allPost(@RequestParam(defaultValue = "0") int pageNo, Model model) {

        int size = 5;
        Page<PostDto> postPage = postService.getPostForCurrentUser(pageNo, size);
        model.addAttribute("postPages", postPage);
        model.addAttribute("currentpage", pageNo);

        return "common/allpost";
    }


    @GetMapping("/addPost")
    public String addPost(Model model) {

        List<CategoryDto> categories = categoryService.getAllCategories();

        PostDto  postDto = new PostDto();
        model.addAttribute("post", postDto);
        model.addAttribute("categories", categories);
        return "common/add-post";
    }

    @PostMapping("/savedPost")
    public String savedPost(@ModelAttribute("post") @Valid PostDto postDto, BindingResult  bindingResult,
                            RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile imagePart, Model model) {


        // check category error ----


        // check validation errors
        if (bindingResult.hasErrors() || imagePart.isEmpty()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("post", postDto);
            model.addAttribute("error", "Please upload image!");
            return "common/add-post";
        }

        // set url title
        String convertedTitle = Helper.getPostTitle(postDto.getTitle());
        postDto.setUrl(convertedTitle);

        String imageName = "";
        // image part check
        if (!imagePart.isEmpty()) {
             imageName =  StringUtils.cleanPath(Objects.requireNonNull(imagePart.getOriginalFilename()));
            postDto.setPostImageName(imageName);
        } else {
            redirectAttributes.addFlashAttribute("error", "Please upload image!");
            return "redirect:/blogdashborad/addPost";
        }

        PostDto savedPostDto = postService.savePost(postDto);
        if (savedPostDto != null) {
            String uploadDirectryPath = System.getProperty("user.dir") + "/postfiles/" + savedPostDto.getId() + "/";

           boolean savedImage =  Helper.saveFile(uploadDirectryPath, imageName, imagePart);

           if (savedImage) {
               redirectAttributes.addFlashAttribute("msg", "Post saved successfully done!");

           }else {
               redirectAttributes.addFlashAttribute("error", "something went wrong image not saved!");
           }
        } else  {
            redirectAttributes.addFlashAttribute("error", "something went wrong!");
        }

        return "redirect:/blogdashborad/addPost";
    }

    @GetMapping("/approvePost")
    public String approvePost(@RequestParam("postId") int postId, @RequestParam("status") String status, @RequestParam(value = "pageNo", defaultValue = "0") int pageNo, RedirectAttributes redirectAttributes) {
        postService.approvePost(postId, status);
        return "redirect:/blogdashborad/allPost?pageNo=" + pageNo;
    }

    @GetMapping("/rejectPost")
    public String rejectPost(@RequestParam("postId") int postId, @RequestParam("status")
                              String status, @RequestParam(value = "pageNo", defaultValue = "0")
                              int pageNo, RedirectAttributes redirectAttributes) {
        postService.rejectPost(postId, status);
        redirectAttributes.addFlashAttribute("msg", "Post " + status + " successfully done!");
        return "redirect:/blogdashborad/allPost?pageNo=" + pageNo;
    }

    @GetMapping("/allPost/view/{postId}")
    public String viewPost(@PathVariable("postId") int postId, Model model) {
        PostDto postDto = postService.getPostById(postId);
        model.addAttribute("post", postDto);
        return "common/viewpost";
    }

    // Delete post in db and image delete in folder
    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") int postId, @RequestParam(value = "pageNo", defaultValue = "0") int pageNo, RedirectAttributes redirectAttributes) {
        postService.deletePost(postId);
        redirectAttributes.addFlashAttribute("msg", "Post deleted successfully done!");
        return "redirect:/blogdashborad/allPost?pageNo=" + pageNo;
    }

    // update-post ----

    @GetMapping("/updatePost/{postId}")
    public String updatePost(@PathVariable("postId") int postId, Model model) {
        PostDto postDto = postService.getPostById(postId);
        model.addAttribute("post", postDto);
        model.addAttribute("categories", categoryService.getAllCategories()); // Add this line
        return "common/edit-post";
    }

    // This is the missing part to SAVE the updated post
    @PostMapping("/updatePost")
    public String updatePostSubmit(@ModelAttribute("post") @Valid PostDto postDto, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile imagePart, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "common/edit-post";
        }

        String convertedTitle = Helper.getPostTitle(postDto.getTitle());
        postDto.setUrl(convertedTitle);

        // Handle image update
        if (!imagePart.isEmpty()) {
            String imageName = StringUtils.cleanPath(Objects.requireNonNull(imagePart.getOriginalFilename()));
            postDto.setPostImageName(imageName);
            String uploadDirectryPath = System.getProperty("user.dir") + "/postfiles/" + postDto.getId() + "/";
            Helper.saveFile(uploadDirectryPath, imageName, imagePart);
        }

        postService.updatePost(postDto);
        redirectAttributes.addFlashAttribute("msg", "Post updated successfully!");

        return "redirect:/blogdashborad/allPost";
    }

    // display all comment ------

    @GetMapping("/admin/allcomment")
    public String allComment(@RequestParam(defaultValue = "0") int pageNo, Model model) {
        int size = 5;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());
        Page<CommentDto> commentPage = commentService.getAllComments(pageable);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("currentPage", pageNo);
        return "admin/allcomment";
    }

    // --- comment delete by comment id ---
    @GetMapping("/admin/delete/{id}")
    public String deleteComment(@PathVariable("id") Long  id, @RequestParam(defaultValue = "0") int pageNo) {


        commentService.deleteComment(id);

        Pageable pageable = PageRequest.of(pageNo, 5); // 5 = page size (same as listing)

        // 3. Fetch updated page
        Page<CommentDto> page = commentService.getAllComments(pageable);

        // 3. If current page empty & not first page → go back
        if(page.getContent().isEmpty() && pageNo > 0){
            pageNo--;
        }

        return  "redirect:/blogdashborad/admin/allcomment?pageNo=" + pageNo;

    }


}