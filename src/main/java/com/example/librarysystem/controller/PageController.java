package com.example.librarysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/public")
    public String publicPage() {
        return "public";
    }

    @GetMapping
    public String userDashboard() {
        return "user-dashboard"; //kräver user eller admin
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard"; //kräver admin
    }
}
