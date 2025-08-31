package com.dms.kalari.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.dms.kalari.admin.service.AuthUserPrivilegeService;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.admin.service.MisDesignationService;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.util.XorMaskHelper;
import com.dms.kalari.admin.dto.AuthUserPrivilegeDTO;
import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.dto.PageWithOperations;

import java.util.Arrays;
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
	    designations = designations.stream()
	            .filter(d -> d.getDesigId() != null)
	            .collect(Collectors.toList());

	    List<Node.Type> nodeTypes = Arrays.asList(Node.Type.values());
	    model.addAttribute("nodeTypes", nodeTypes);

	    model.addAttribute("roles", designations);

	    // 👇 New: Group designations by level
	    Map<Node.Type, List<DesignationDTO>> designationMap = designations.stream()
	            .collect(Collectors.groupingBy(DesignationDTO::getDesigLevel));
	    model.addAttribute("designationMap", designationMap);

	    return "fragments/admin/permissions/index";
	}


	@GetMapping("/byrole/{moduleId}/{roleId}/{nodeType}")
	public String permission(@PathVariable(value = "roleId", required = true) Long roleId,
			@PathVariable(value = "moduleId", required = true) Long moduleId, 
			@PathVariable(value = "nodeType", required = true) Node.Type nodeType,
			Model model) {

        List<PageWithOperations> permissions = privilegeService.getModulePermissions(roleId, moduleId, nodeType);
        model.addAttribute("permissions", permissions);
        model.addAttribute("roleId", roleId);
        model.addAttribute("moduleId", moduleId);
		model.addAttribute("menuId", moduleId);
		model.addAttribute("level", nodeType);
		model.addAttribute("target", "permissions_target");

		return "fragments/admin/permissions/byrole";
	}

	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updatePermissions(
	        @RequestParam("roleId") Long roleId,
	        @RequestParam("moduleId") Long moduleId,
	        @RequestParam("level") String level,
	        @RequestParam(value = "permissions", required = false) List<String> permissionStrings) {

	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("target", "users_target");

	    try {
	        // Parse the string permissions to extract both pageId and operationId
	        List<Object[]> pageOperationPairs = permissionStrings != null ? 
	            permissionStrings.stream()
	                .map(str -> {
	                    // Assuming format is "pageId_operationId"
	                    String[] parts = str.split("_");
	                    if (parts.length != 2) {
	                        throw new IllegalArgumentException("Invalid permission string format: " + str);
	                    }
	                    Long pageId = Long.parseLong(parts[0]);
	                    Long operationId = Long.parseLong(parts[1]);
	                    return new Object[]{pageId, operationId};
	                })
	                .collect(Collectors.toList()) : 
	            Collections.emptyList();
	            
	        service.updatePrivileges(roleId, moduleId, pageOperationPairs);
	        return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "success", true,
	            "message", "Details updated successfully",
	            "loadnext", "permissions_byrole/"+ moduleId+"/"+roleId+"/"+level,
	            "target", "permissions_target"
	        ));
	    } catch (Exception e) {
	        e.printStackTrace();
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