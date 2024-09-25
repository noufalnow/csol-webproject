package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.CoreUserService;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserToggleDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.dto.DesignationDTO;

import com.example.tenant_service.service.MisDesignationService;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class CoreUserHtmlController extends BaseController<CoreUserDTO, CoreUserService> {
	
	
    private final MisDesignationService designationService;

    // Inject both services via constructor
    public CoreUserHtmlController(CoreUserService coreUserService, MisDesignationService designationService) {
        super(coreUserService);
        this.designationService = designationService; // Properly assign the designationService
    }


    @GetMapping("/html")
    public String listUsers(
    	    @RequestParam(defaultValue = "0") int page,
    	    @RequestParam(defaultValue = "10") int size,
    	    @RequestParam(defaultValue = "userId") String sortField,
    	    @RequestParam(defaultValue = "asc") String sortDir,
    	    @RequestParam(required = false) String search,
    	    Model model) {


    	    // Construct the Pageable object
    	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

    	    logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}",
    	            page, size, sortField, sortDir, search);

    	    Page<CoreUserDTO> userPage = service.findAllPaginate(pageable, search);

    	    logInfo("User Page - Total Elements: {}, Total Pages: {}", userPage.getTotalElements(), userPage.getTotalPages());
    	    logInfo("Users: {}", userPage.getContent());

    	    // Use the reusable method for setting up pagination
    	    setupPagination(model, userPage, sortField, sortDir);

    	    model.addAttribute("search", search);
    	    model.addAttribute("pageTitle", "User List - My Application");
            model.addAttribute("pageUrl",  "/users/html");

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
        // Fetch designations from the service and pass them to the model
        List<DesignationDTO> designations = designationService.findAll();
        model.addAttribute("designations", designations);
        return "fragments/add_user";
    }

    @PostMapping("/html/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO,
                                                       BindingResult result) {
    	
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/users/html");
        return handleRequest(result, () -> service.save(userDTO), "User added successfully", additionalData);
    }

    @GetMapping("/html/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("userDTO", service.findById(id));
        List<DesignationDTO> designations = designationService.findAll();
        model.addAttribute("designations", designations);
        return "fragments/edit_user";
    }

    @PostMapping("/html/update/{refId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
                                                          @Valid @ModelAttribute CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/users/html");

        return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully", additionalData);
    }

    @PostMapping("/html/resetPassword/{refId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
                                                             @Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/users/html");

        return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully", additionalData);
    }

    @PostMapping("/html/toggleStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
        boolean isActive = service.toggleUserStatus(toggleStatusDTO.getUserId(), toggleStatusDTO.getUserStatus());
        return buildResponse(isActive ? "User activated successfully" : "User deactivated successfully",
                Map.of("active", isActive ? "Y" : "N"));
    }

    @GetMapping("/html/resetPassword/{id}")
    public String resetPassword(@PathVariable("id") Long id, Model model) {
        CoreUserDTO user = service.findById(id); // Use the service instance
        model.addAttribute("userDTO", user);
        return "fragments/reset_password"; // View for updating user details
    }
}

