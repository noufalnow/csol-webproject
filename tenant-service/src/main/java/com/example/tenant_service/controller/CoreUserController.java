package com.example.tenant_service.controller;

import com.example.tenant_service.dto.CoreUserDTO;
import com.example.tenant_service.service.CoreUserService;
import com.example.tenant_service.common.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class CoreUserController extends BaseController<CoreUserDTO, CoreUserService> {

    public CoreUserController(CoreUserService coreUserService) {
        super(coreUserService);
    }

    @PostMapping(value = "/html/add", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody CoreUserDTO userDTO, BindingResult result) {
        Map<String, String> validationErrors = isValid(result);
        
        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Validation failed - Error occurred");
            errorResponse.put("errors", validationErrors);
            errorResponse.put("status", "error");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        // Proceed with user creation if no validation errors
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", "User added successfully");
        successResponse.put("status", "success");
        return ResponseEntity.ok(successResponse);
    }
}
