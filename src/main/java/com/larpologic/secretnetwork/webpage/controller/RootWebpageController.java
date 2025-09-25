package com.larpologic.secretnetwork.webpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootWebpageController {

    @GetMapping("/")
    public String showLoginPage() {
        return "centrum/index";
    }

    @GetMapping("/unauthorized")
    public String showUnauthorizedPage() {
        return "unauthorized";
    }

   @GetMapping("/login")
    public String login() {
        return "login";
    }
}