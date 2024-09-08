package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.CoreUserService;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserToggleDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class CoreUserHtmlController extends BaseController<CoreUserDTO, CoreUserService> {

	// Constructor injection is handled by BaseController
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
	public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO,
			BindingResult result) {
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
			successResponse.put("loadnext", "/users/html");
			successResponse.put("status", "success");
			return ResponseEntity.ok(successResponse);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "An error occurred while adding the user: " + e.getMessage());
			errorResponse.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Edit User
	@GetMapping("/html/edit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
		CoreUserDTO user = service.findById(id); // Use the service instance
		model.addAttribute("userDTO", user);
		return "fragments/edit_user"; // View for updating user details
	}

	@PostMapping("/html/update/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
			@Valid @ModelAttribute CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
		Map<String, String> validationErrors = isValid(result);

		if (!validationErrors.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Validation failed - Error occurred");
			response.put("errors", validationErrors);
			response.put("status", "error");
			return ResponseEntity.badRequest().body(response);
		}

		// Proceed with updating user details if no validation errors
		try {
			CoreUserDTO updatedUserDTO = service.updateUser(userId, coreUserUpdateDTO); // Update user using the service
																						// layer

			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("message", "User updated successfully");
			successResponse.put("loadnext", "/users/html");
			successResponse.put("status", "success");
			return ResponseEntity.ok(successResponse);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "An error occurred while updating the user: " + e.getMessage());
			errorResponse.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/html/resetPassword/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
	        @Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
	    Map<String, Object> response = new HashMap<>();

	    // Step 1: Validate the request data
	    Map<String, String> validationErrors = isValid(result);

	    if (!validationErrors.isEmpty()) {
	        response.put("message", "Validation failed - Error occurred");
	        response.put("errors", validationErrors);
	        response.put("status", "error");
	        return ResponseEntity.badRequest().body(response);
	    }

	    // Step 2: Attempt to reset the password using the service
	    try {
	        CoreUserDTO updatedUserDTO = service.resetPassword(refId, passwordDTO);

	        response.put("message", "Password reset successfully");
	        response.put("loadnext", "/users/html");
	        response.put("status", "success");
	        return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	        // Handling specific errors like new password mismatch
	        response.put("message", e.getMessage());
	        response.put("errors", new HashMap<>()); // No field-specific errors
	        response.put("status", "error");
	        return ResponseEntity.badRequest().body(response);
	    } catch (Exception e) {
	        // Step 3: Handle any general exception
	        response.put("message", "An error occurred while resetting the password: " + e.getMessage());
	        response.put("errors", new HashMap<>()); // No field-specific errors
	        response.put("status", "error");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



	@PostMapping("html/toggleStatus")
	@ResponseBody
	public Map<String, Object> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
	    Long userId = toggleStatusDTO.getUserId(); // Capture the user ID
	    Short userStatus = toggleStatusDTO.getUserStatus(); // Capture the new status

	    // Call the service to toggle the user status
	    boolean isActive = service.toggleUserStatus(userId, userStatus);

	    // Prepare the response
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", 1);  // Assuming 1 means success
	    response.put("active", isActive ? "Y" : "N");  // Y for active, N for inactive

	    return response;
	}








	// Reset User Password
	@GetMapping("/html/resetPassword/{id}")
	public String resetPassword(@PathVariable("id") Long id, Model model) {
		CoreUserDTO user = service.findById(id); // Use the service instance
		model.addAttribute("userDTO", user);
		return "fragments/reset_password"; // View for updating user details
	}

}
