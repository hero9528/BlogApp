package com.asr.blogapp.controller;


import com.asr.blogapp.dto.CategoryDto;
import com.asr.blogapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin/category")
public class PostCategoryController {

    @Autowired
    private CategoryService categoryService;


    // to display all categories
    @GetMapping("/addcategory")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "admin/postcategory";

    }
    // save to category
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") @Valid CategoryDto categoryDto,
                               BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDto);
            return "admin/postcategory";
        }
        try {
            categoryService.saveCategory(categoryDto);
            redirectAttributes.addFlashAttribute("msg", "Category added Done!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error","category already exists");
            return "redirect:/admin/category/addcategory";
        }

        return "redirect:/admin/category/addcategory";
    }
}