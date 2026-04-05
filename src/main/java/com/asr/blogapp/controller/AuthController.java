package com.asr.blogapp.controller;

import com.asr.blogapp.dto.UserSignupDto;
import com.asr.blogapp.service.UserService;
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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService  userService;

    // login from ----

    @GetMapping("/login-user")
    public String showLoginForm(Model  model) {


        return "auth/login";
    }

    // signup from ----

    @GetMapping("/signup-user")
    public String showSignupForm(Model  model) {
        model.addAttribute("user", new UserSignupDto());

        return "auth/signup";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") UserSignupDto userDto,
                           BindingResult bindingResult,   // ✅ immediately after @Valid
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return "auth/signup";
        }

        try {
            userService.saveUser(userDto);
        } catch (RuntimeException e) {
            model.addAttribute("error", "user already exists...");   // ✅ yaha message bhej rahe
            return "auth/signup";                          // ✅ same page pe wapas
        }
        // userService.saveUser(userDto);
        redirectAttributes.addFlashAttribute("msg", "User Registration Successful");
        return "redirect:/blog/publicpost";

    }
}
