package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.CoreUserService;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserToggleDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class CoreUserHtmlController extends BaseController<CoreUserDTO, CoreUserService> {

    public CoreUserHtmlController(CoreUserService coreUserService) {
        super(coreUserService);
    }

    @GetMapping("/html")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "userId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<CoreUserDTO> userPage = service.findAllPaginate(pageable, search);

        int totalPages = userPage.getTotalPages();
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        Map<String, String> sortStatus = new HashMap<>();
        sortStatus.put(sortField, sortDir);
        model.addAttribute("sortStatus", sortStatus);
        
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        
        model.asMap().forEach((key, value) -> {
            System.out.println("Model attribute key: " + key + ", value: " + value);
        });


        // Determine page range for display (e.g., show 2 pages before and 2 pages after the current page)
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
		model.addAttribute("user", new CoreUserDTO());
		return "fragments/add_user";
	}

	@PostMapping("/html/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO,
			BindingResult result) {
		return handleRequest(result, () -> service.save(userDTO), "User added successfully",null);
	}

	@GetMapping("/html/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		model.addAttribute("userDTO", service.findById(id));
		return "fragments/edit_user";
	}
	
	
	@PostMapping("/html/update/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
	        @Valid @ModelAttribute CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
	    
	    // Define the additional data you want to return in the response, e.g., reload link
	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("loadnext", "/users/html");
	    
	    // Call handleRequest with the success message and the additional data
	    return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully", additionalData);
	}

	@PostMapping("/html/resetPassword/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
			@Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
		
		
	    // Define the additional data you want to return in the response, e.g., reload link
	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("loadnext", "/users/html");
		
		return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully",additionalData);
	}

	@PostMapping("/html/toggleStatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
		boolean isActive = service.toggleUserStatus(toggleStatusDTO.getUserId(), toggleStatusDTO.getUserStatus());
		return buildResponse(isActive ? "User activated successfully" : "User deactivated successfully",
				Map.of("active", isActive ? "Y" : "N"));
	}

	// Reset User Password
	@GetMapping("/html/resetPassword/{id}")
	public String resetPassword(@PathVariable("id") Long id, Model model) {
		CoreUserDTO user = service.findById(id); // Use the service instance
		model.addAttribute("userDTO", user);
		return "fragments/reset_password"; // View for updating user details
	}

}
