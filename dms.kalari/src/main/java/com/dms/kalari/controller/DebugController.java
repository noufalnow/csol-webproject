package com.dms.kalari.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DebugController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/current-user")
    @ResponseBody
    public String currentUser(Authentication authentication) {
        return "Current user: " + (authentication != null ? authentication.getName() : "none");
    }
}

