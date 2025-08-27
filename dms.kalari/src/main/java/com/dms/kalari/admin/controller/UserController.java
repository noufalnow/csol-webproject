package com.dms.kalari.admin.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserPasswordDTO;
import com.dms.kalari.admin.dto.CoreUserToggleDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.dto.OfficialUpdateDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.dto.OfficialAddDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.mapper.CoreUserMapper;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.admin.service.MisDesignationService;
import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.security.CustomUserPrincipal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import com.dms.kalari.admin.repository.CoreUserRepository;

@Controller
@RequestMapping("/admin/users")
public class UserController extends BaseController<CoreUserDTO, CoreUserService> {

	private final MisDesignationService designationService;
	// Inject both services via constructor
	public UserController(CoreUserService coreUserService, MisDesignationService designationService,
            CoreUserRepository coreUserRepository,
            CoreUserMapper coreUserMapper,
			NodeService nodeService) {
		super(coreUserService);
		this.designationService = designationService; // Properly assign the designationService
	}

	@GetMapping("/")
	//@PreAuthorize("@privilegeChecker.hasAccess('/admin/users', 'GET')")
	
	public String listUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortField, @RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(required = false) String search, Model model) {

		// Construct the Pageable object
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}", page, size,
				sortField, sortDir, search);

		Page<CoreUserDTO> userPage = service.findAllPaginate(pageable, search);

		logInfo("User Page - Total Elements: {}, Total Pages: {}", userPage.getTotalElements(),
				userPage.getTotalPages());
		logInfo("Users: {}", userPage.getContent());

		// Use the reusable method for setting up pagination
		setupPagination(model, userPage, sortField, sortDir);

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "User List ");
		model.addAttribute("pageUrl", "/users");

		return "fragments/admin/users/core_user_list";
	}
	
	
	@GetMapping("/byeglobal")
	public String listUsersByNodeBlobal(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long nodeId = (Long) session.getAttribute("NODE_ID");

		List<CoreUser> users = service.listUsersByNode(nodeId);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");
		return "fragments/admin/users/node_users";
	}
	
	
	@GetMapping("/add")
	public String showAddUserForm(Model model) {
		model.addAttribute("pageTitle", "Add User ");
		model.addAttribute("user", new CoreUserDTO());
		// Fetch designations from the service and pass them to the model
		List<DesignationDTO> designations = designationService.findAll();
		model.addAttribute("designations", designations);
		return "fragments/admin/users/add_user";
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO,
			BindingResult result) {

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");
		return handleRequest(result, () -> service.save(userDTO), "User added successfully", additionalData);
	}
	
	


	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		model.addAttribute("userDTO", service.findById(id));
		List<DesignationDTO> designations = designationService.findAll();
		model.addAttribute("designations", designations);
		return "fragments/admin/users/edit_user";
	}

	@PostMapping("/edit/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
			@Valid @ModelAttribute CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");

		return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully",
				additionalData);
	}

	@PostMapping("/resetpassword/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
			@Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");

		return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully",
				additionalData);
	}

	@PostMapping("/togglestatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
		boolean isActive = service.toggleUserStatus(toggleStatusDTO.getUserId(), toggleStatusDTO.getUserStatus());
		return buildResponse(isActive ? "User activated successfully" : "User deactivated successfully",
				Map.of("active", isActive ? "Y" : "N"));
	}

	@GetMapping("/resetpassword/{id}")
	public String resetPassword(@PathVariable("id") Long id, Model model) {
		CoreUserDTO user = service.findById(id); // Use the service instance
		model.addAttribute("userDTO", user);
		return "fragments/admin/users/reset_password"; // View for updating user details
	}
}
