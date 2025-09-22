package com.larpologic.secretnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String showLoginPage() {
        return "badania/index";
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