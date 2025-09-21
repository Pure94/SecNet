package com.larpologic.secretnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @GetMapping("/")
    public String showLoginPage() {
        return "login/index";
    }

    @PostMapping("/login")
    public String performLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        if ("operator".equals(username) && "secret".equals(password)) {
            return "redirect:/badania";
        } else {
            model.addAttribute("loginError", "Nieprawidłowa nazwa użytkownika lub hasło. Dostęp zastrzeżony.");
            return "login/index";
        }
    }
}