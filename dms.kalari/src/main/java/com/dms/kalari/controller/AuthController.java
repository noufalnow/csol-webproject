package com.dms.kalari.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    

    @GetMapping("/auth-error")
    public String authError(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        model.addAttribute("errorMessage",
                error != null ? error : "Authentication failed. Please try again.");
        return "auth-error"; // name of the Thymeleaf template
    }
}


