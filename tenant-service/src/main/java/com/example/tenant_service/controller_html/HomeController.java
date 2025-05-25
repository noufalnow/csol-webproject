package com.example.tenant_service.controller_html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.tenant_service.service.CoreUserService;
import com.example.tenant_service.dto.users.CoreUserDTO;

@Controller
public class HomeController {
	
    private final CoreUserService coreUserService;

    @Autowired  // Constructor injection (recommended)
    public HomeController(CoreUserService coreUserService) {
        this.coreUserService = coreUserService;
    }

	@GetMapping("/home")
    public String home(Authentication authentication, Model model, HttpServletRequest request) {
        model.addAttribute("pageTitle", "Home - My Application");
        model.addAttribute("content", "fragments/profile/profile");
        
        
        String userEmail = authentication.getName();
        CoreUserDTO userDto  = coreUserService.getUserByEmailAddress(userEmail);
        
        HttpSession session = request.getSession();
        session.setAttribute("ParentId", userDto.getUserNode());

        return "layout";
    }

}

