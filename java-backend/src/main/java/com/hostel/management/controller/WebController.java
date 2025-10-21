package com.hostel.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main web controller for serving frontend pages
 */
@Controller
public class WebController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/admin")
    public String adminLogin() {
        return "admin_login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/admin_dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }
    
    @GetMapping("/preferences")
    public String preferences() {
        return "preferences";
    }
}