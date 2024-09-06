package com.example.tenant_service.controller_html;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String home(Model model) {
	    model.addAttribute("pageTitle", "Home - My Application");
	    model.addAttribute("content", "fragments/home");
	    return "layout";
	}

}

