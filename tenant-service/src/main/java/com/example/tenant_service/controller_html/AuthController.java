package com.example.tenant_service.controller_html;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    
    

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}


