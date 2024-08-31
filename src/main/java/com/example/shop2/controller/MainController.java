package com.example.shop2.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if (session != null && session.getAttribute("email") != null) {
            model.addAttribute("login", true);
        } else {
            model.addAttribute("login", false);
        }

        return "main";
    }
}
