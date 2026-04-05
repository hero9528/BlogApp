package com.asr.blogapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashborad")
public class DashboradController {


    @GetMapping("/admin")
    public String showDashboradAdmin() {
        return "/admin/AdminPage";
    }

    @GetMapping("/guest")
    public String showDashboradGuest() {
        return "/guest/GuestDashborad";
    }

}
