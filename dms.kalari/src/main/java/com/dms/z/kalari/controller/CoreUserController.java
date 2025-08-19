package com.dms.z.kalari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserPasswordDTO;
import com.dms.kalari.admin.dto.CoreUserToggleDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.common.BaseController;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class CoreUserController extends BaseController<CoreUserDTO, CoreUserService> {

    public CoreUserController(CoreUserService coreUserService) {
        super(coreUserService);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<CoreUserDTO>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "userId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        // Construct the Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        
        // Fetch the paginated and filtered users
        Page<CoreUserDTO> userPage = service.findAllPaginate(pageable, search);
        
        return ResponseEntity.ok(userPage);
    }

    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody CoreUserDTO userDTO, BindingResult result) {
        Map<String, String> validationErrors = validate(result);
        
        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Validation failed - Error occurred");
            errorResponse.put("errors", validationErrors);
            errorResponse.put("status", "error");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Proceed with user creation if no validation errors
        CoreUserDTO createdUser = service.save(userDTO); // Save user
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", "User added successfully");
        successResponse.put("status", "success");
        successResponse.put("user", createdUser); // Include created user details in response
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CoreUserDTO> getUserById(@PathVariable Long id) {
        CoreUserDTO user = service.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update/{refId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
                                                          @Valid @RequestBody CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/api/users");

        return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully", additionalData);
    }

    @PostMapping("/resetPassword/{refId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
                                                             @Valid @RequestBody CoreUserPasswordDTO passwordDTO, BindingResult result) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/api/users");

        return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully", additionalData);
    }

    @PostMapping("/toggleStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
        boolean isActive = service.toggleUserStatus(toggleStatusDTO.getUserId(), toggleStatusDTO.getUserStatus());
        return buildResponse(isActive ? "User activated successfully" : "User deactivated successfully",
                Map.of("active", isActive ? "Y" : "N"));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CoreUserDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CoreUserDTO> searchResults = service.searchUsers(query, pageable);
        return ResponseEntity.ok(searchResults);
    }
}
