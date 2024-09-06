package com.example.tenant_service.controller_html;

import com.example.tenant_service.dto.CoreUserDTO;
import com.example.tenant_service.service.CoreUserService;

import jakarta.validation.Valid;

import com.example.tenant_service.common.BaseController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class CoreUserHtmlController extends BaseController<CoreUserDTO, CoreUserService> {

    public CoreUserHtmlController(CoreUserService coreUserService) {
        super(coreUserService);
    }

    @GetMapping("/html")
    public String listUsers(Model model) {
        model.addAttribute("users", service.findAll());
        model.addAttribute("pageTitle", "User List - My Application");
        return "fragments/core_user_list";
    }

    @GetMapping("/html/{id}")
    public String viewUserById(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findById(id));
        model.addAttribute("pageTitle", "User Detail - My Application");
        return "fragments/core_user_detail";
    }

    @GetMapping("/html/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("pageTitle", "Add User - My Application");
        model.addAttribute("user", new CoreUserDTO()); // Initialize an empty user DTO
        return "fragments/add_user";
    }

    @PostMapping("/html/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO, BindingResult result) {
        Map<String, String> validationErrors = isValid(result);
        
        if (!validationErrors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Validation failed - Error occurred");
            response.put("errors", validationErrors);
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Proceed with user creation if no validation errors
        try {
            CoreUserDTO savedUserDTO = service.save(userDTO); // Save user using the service layer
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "User added successfully");
            successResponse.put("status", "success");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An error occurred while adding the user: " + e.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
