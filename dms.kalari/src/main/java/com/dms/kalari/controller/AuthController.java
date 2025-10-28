package com.dms.kalari.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/auth-error")
	public String authError(@RequestParam(value = "error", required = false) String error, Model model) {
		model.addAttribute("errorMessage", error != null ? error : "Authentication failed. Please try again.");
		return "auth-error"; // name of the Thymeleaf template
	}

	@GetMapping("/access-denied")
	public String accessDenied(Model model) {
		model.addAttribute("errorMessage", "You do not have permission to access this page.");
		return "access-denied"; // must match the template name
	}

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;

		String message;
		switch (statusCode) {
		case 400 -> message = "Bad request. Please check your input.";
		case 401 -> message = "You are not authorized to access this page.";
		case 403 -> message = "Access denied. You don’t have permission to view this page.";
		case 404 -> message = "The page you’re looking for doesn’t exist.";
		case 500 -> message = "Internal server error. Please try again later.";
		default -> message = "An unexpected error occurred.";
		}

		model.addAttribute("status", statusCode);
		model.addAttribute("message", message);
		return "error"; // Maps to error.html template
	}
}
