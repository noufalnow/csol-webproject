package com.dms.kalari.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.dms.kalari.admin.service.AuthUserPrivilegeService;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.admin.service.MisDesignationService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.util.XorMaskHelper;
import com.dms.kalari.admin.dto.AuthUserPrivilegeDTO;
import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.dto.PageWithOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/permissions")
public class PermissionsController extends BaseController<AuthUserPrivilegeDTO, AuthUserPrivilegeService> {

	private final AuthUserPrivilegeService privilegeService;
	private final MisDesignationService designationService;

	public PermissionsController(AuthUserPrivilegeService privilegeService, MisDesignationService designationService) {
		super(privilegeService);
		this.privilegeService = privilegeService;
		this.designationService = designationService;
	}

	@GetMapping({ "/index", "/index/" })
	public String index(Model model) {
		List<DesignationDTO> designations = designationService.findAll();

		designations = designations.stream().filter(d -> d.getDesigId() != null).collect(Collectors.toList());

		model.addAttribute("roles", designations);
		return "fragments/admin/permissions/index";
	}

	@GetMapping("/byrole/{moduleId}/{roleId}")
	public String permission(@PathVariable(value = "roleId", required = true) Long roleId,
			@PathVariable(value = "moduleId", required = true) Long moduleId, Model model) {

        List<PageWithOperations> permissions = privilegeService.getModulePermissions(roleId, moduleId);
        model.addAttribute("permissions", permissions);
        model.addAttribute("roleId", roleId);
        model.addAttribute("moduleId", moduleId);
		model.addAttribute("menuId", moduleId);
		model.addAttribute("target", "permissions_target");

		return "fragments/admin/permissions/byrole";
	}

	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updatePermissions(
	        @RequestParam("roleId") Long roleId,
	        @RequestParam("moduleId") Long moduleId,
	        @RequestParam(value = "permissions", required = false) List<Long> permissionIds) { // Change to List<Long>

	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("target", "users_target");

	    try {
	        service.updatePrivileges(roleId, moduleId, permissionIds != null ? permissionIds : Collections.emptyList());
	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "message", "Details updated successfully",
	            "additionalData", additionalData
	        ));
	    } catch (Exception e) {
	        e.printStackTrace(); // Add logging
	        return ResponseEntity.badRequest().body(Map.of(
	            "success", false,
	            "message", "Error updating permissions: " + e.getMessage()
	        ));
	    }
	}

	private boolean isCurrentUserAdmin() {
		// Implement your admin check logic
		return true; // placeholder
	}
}