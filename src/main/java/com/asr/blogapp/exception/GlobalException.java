package com.asr.blogapp.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e , Model model) {
        model.addAttribute("error", "Unexpected server error occurred. Please try again later.");
        return "errorpage";
    }
}
