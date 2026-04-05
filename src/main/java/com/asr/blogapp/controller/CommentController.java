package com.asr.blogapp.controller;

import com.asr.blogapp.dto.CommentDto;
import com.asr.blogapp.dto.PostDto;
import com.asr.blogapp.service.CommentService;
import com.asr.blogapp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/feedback")
    public String commentPost(@ModelAttribute("comment") @Valid CommentDto commentDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
           PostDto postDto = postService.getPostById(commentDto.getPostId());

           List<CommentDto> comments = commentService.getCommentsByPostId(postDto.getId());

            List<CommentDto> commentTop = commentService.getTop5CommentsByPostId(postDto.getId());

           model.addAttribute("post", postDto);
           model.addAttribute("comment", commentDto);
           model.addAttribute("comments", comments);
           model.addAttribute("comments", commentTop);

           return "blog/fullview";
        }
        commentService.addComment(commentDto);

        PostDto postDto = postService.getPostById(commentDto.getPostId());
        redirectAttributes.addFlashAttribute("successMessage", "Your feedback has been submitted successfully!");
        return "redirect:/blogfullview/"+postDto.getId()+"/"+postDto.getUrl();
    }
}