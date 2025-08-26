package com.dms.kalari.admin.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserPasswordDTO;
import com.dms.kalari.admin.dto.CoreUserToggleDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateMemberDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.dto.CoreUserMemberDTO;
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
@RequestMapping("/admin")
public class CoreUserController extends BaseController<CoreUserDTO, CoreUserService> {

	private final MisDesignationService designationService;
	private final NodeService nodeService;
    private final CoreUserRepository coreUserRepository; // Add this
    private final CoreUserMapper coreUserMapper; // Add this

	// Inject both services via constructor
	public CoreUserController(CoreUserService coreUserService, MisDesignationService designationService,
            CoreUserRepository coreUserRepository,
            CoreUserMapper coreUserMapper,
			NodeService nodeService) {
		super(coreUserService);
		this.designationService = designationService; // Properly assign the designationService
		this.nodeService = nodeService;
		  this.coreUserRepository = coreUserRepository;
	        this.coreUserMapper = coreUserMapper;
	}

	@GetMapping("/users")
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

	@GetMapping({"/users/bynode/", "/users/bynode/{id}", "/users/bynodeoff/", "/users/bynodeoff/{id}"})
	public String listUsersByNode(
	    @PathVariable(value = "id", required = false) Long nodeId,
	    HttpServletRequest request,
	    HttpSession session,
	    Model model) {

	    // Determine if this is for members or officials
	    boolean isOfficialRequest = request.getRequestURI().contains("/bynodeoff");
	    
	    // Set or get node ID
	    if (nodeId != null) {
	        session.setAttribute("ParentId", nodeId); // for data entry
	    } else {
	        nodeId = (Long) session.getAttribute("NODE_ID");
	    }

	    // Get node information
	    NodeDTO node = nodeService.findById(nodeId);
	    Node.Type nodeType = node.getNodeType();
	    Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");

	    // Log node information
	    logInfo("My Node ID: {}", session.getAttribute("NODE_ID"));
	    logInfo("Selected Node ID: {}", node.getNodeId());

	    // Set model attributes based on request type and node types
	    if (!isOfficialRequest) {
	        model.addAttribute("pageTitle", "Members");
	        if ((userNodeType == Node.Type.KALARI && nodeType == Node.Type.KALARI) && 
	            (node.getNodeId().equals(session.getAttribute("NODE_ID")))) {
	            model.addAttribute("allowAddMember", true);
	        }
	        if (nodeType == Node.Type.KALARI) {
	            model.addAttribute("showMemberPanel", true);
	        }
	    } else { // Official request
	        model.addAttribute("pageTitle", "Officials");
	        if (nodeType.getLevel() > userNodeType.getLevel() && (userNodeType != Node.Type.KALARI)) {
	            model.addAttribute("allowAddMember", true);
	        }
	        model.addAttribute("showMemberPanel", true);
	    }

	    // Get users based on type
	    UserType userType = isOfficialRequest ? UserType.OFFICIAL : UserType.MEMBER;
	    List<CoreUser> users = service.listUsersByNodeAndType(nodeId, userType);
	    
	    model.addAttribute("parentId", nodeId);
	    model.addAttribute("users", users);
	    model.addAttribute("target", "users_target");
	    model.addAttribute("isOfficialView", isOfficialRequest);
	    
	    return "fragments/admin/users/node_users";
	}


	@GetMapping("/users/details/{id}")
	public String viewUserById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(id));
		model.addAttribute("pageTitle", "User Detail ");
		return "fragments/admin/users/view_member";
	}

	@GetMapping("/users/view/{id}")
	public String viewUserBiewById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(id));
		model.addAttribute("pageTitle", "User Detail ");
		return "fragments/admin/users/profile/view";
	}


	@GetMapping("/users/addmember")
	public String showAddMemberUserForm(Model model, HttpServletRequest request, HttpSession session) {
		model.addAttribute("user", new CoreUserMemberDTO());
		
		Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");

		List<DesignationDTO> designations  = null;
		
		if (userNodeType == Node.Type.KALARI) {
			model.addAttribute("pageTitle", "Add Members");
			designations = designationService.findAllByType((short) 2);
		}
		else
		{
			model.addAttribute("pageTitle", "Add Officials");
			designations = designationService.findAllByType((short) 1);
		}
		
		logInfo("My Node userNodeType: {}", userNodeType);
		logInfo("My Node Node.Type.KALARI: {}", userNodeType);

		model.addAttribute("designations", designations);

		return "fragments/admin/users/add_member";
	}
	
	
	@PostMapping("/users/addmember")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addMember(@Valid @ModelAttribute CoreUserMemberDTO CoreUserMemberDTO,
			BindingResult result, HttpServletRequest request) {

		logInfo("Request Parameters – CoreUserMemberDTO: {}", CoreUserMemberDTO);

		HttpSession session = request.getSession(false);
		Long parentId = null;
		if (session != null) {
			Object attr = session.getAttribute("ParentId");
			if (attr instanceof Long) {
				parentId = (Long) attr;
			} else if (attr instanceof String) {
				parentId = Long.valueOf((String) attr);
			}
		}
		if (parentId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "ParentId not found in session"));
		}

		logInfo("Request Parameters – setUserNodeId-parentId: {}", parentId);

		CoreUserMemberDTO.setUserNode(parentId);

		if (!Node.Type.KALARI.equals(session.getAttribute("NODE_TYPE"))) {
			CoreUserMemberDTO.setUserType(CoreUser.UserType.OFFICIAL);
		}

		/*System.out.println("=== Session Attributes ===");
		Collections.list(session.getAttributeNames())
				.forEach(name -> System.out.println(name + " = " + session.getAttribute(name)));*/

		CoreUserMemberDTO.setUserPassword("123456");
		CoreUserMemberDTO.setUserUname("uname");

		Map<String, Object> additionalData = new HashMap<>();
		if (Node.Type.KALARI.equals(session.getAttribute("NODE_TYPE"))) 
			additionalData.put("loadnext", "users_bynode/" + parentId);
		else
			additionalData.put("loadnext", "users_bynodeoff/" + parentId);
			
		additionalData.put("target", "users_target");
		return handleRequest(result, () -> service.saveMamber(CoreUserMemberDTO), "User added successfully",
				additionalData);
	}
	
	
	@GetMapping("/users/editmember/{id}")
	public String editMember(@PathVariable Long id, Model model, HttpSession session) {
	    // Get the user entity
	    CoreUser user = coreUserRepository.findByIdAndNotDeleted(id)
	            .orElseThrow(() -> new ResourceNotFoundException("CoreUser", id));
	    
	    // Convert to CoreUserUpdateMemberDTO
	    CoreUserUpdateMemberDTO userDTO = coreUserMapper.toUpdateMemberDTO(user);
	    
	    model.addAttribute("user", userDTO);
	    
	    List<DesignationDTO> designations = null;
	    Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");
	    
	    if (userNodeType == Node.Type.KALARI) {
	        model.addAttribute("pageTitle", "Edit Members");
	        designations = designationService.findAllByType((short) 2);
	    } else {
	        model.addAttribute("pageTitle", "Edit Officials");
	        designations = designationService.findAllByType((short) 1);
	    }
	    model.addAttribute("designations", designations);
	    return "fragments/admin/users/edit_member_user";
	}

	@PostMapping("/users/editmember/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateMember(@PathVariable("refId") Long userId,
			@Valid @ModelAttribute CoreUserUpdateMemberDTO coreUserUpdateDTO, BindingResult result,HttpSession session) {
		Map<String, Object> additionalData = new HashMap<>();
		
		Object parentId = session.getAttribute("ParentId");
		
		
		
		if (Node.Type.KALARI.equals(session.getAttribute("NODE_TYPE"))) 
			additionalData.put("loadnext", "users/html/bynode/" + parentId);
		else
			additionalData.put("loadnext", "users/html/bynodeoff/" + parentId);
		
		
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.updateMember(userId, coreUserUpdateDTO), "User updated successfully",
				additionalData);
	}
	
	
	/*************************************** */
	
	
	@GetMapping("/users/bynodeglobal")
	public String listUsersByNodeBlobal(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long nodeId = (Long) session.getAttribute("NODE_ID");

		List<CoreUser> users = service.listUsersByNode(nodeId);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");
		return "fragments/admin/users/node_users";
	}
	
	
	@GetMapping("/users/add")
	public String showAddUserForm(Model model) {
		model.addAttribute("pageTitle", "Add User ");
		model.addAttribute("user", new CoreUserDTO());
		// Fetch designations from the service and pass them to the model
		List<DesignationDTO> designations = designationService.findAll();
		model.addAttribute("designations", designations);
		return "fragments/admin/users/add_user";
	}

	@PostMapping("/users/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addUser(@Valid @ModelAttribute CoreUserDTO userDTO,
			BindingResult result) {

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");
		return handleRequest(result, () -> service.save(userDTO), "User added successfully", additionalData);
	}
	
	


	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		model.addAttribute("userDTO", service.findById(id));
		List<DesignationDTO> designations = designationService.findAll();
		model.addAttribute("designations", designations);
		return "fragments/admin/users/edit_user";
	}

	@PostMapping("/users/edit/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("refId") Long userId,
			@Valid @ModelAttribute CoreUserUpdateDTO coreUserUpdateDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");

		return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully",
				additionalData);
	}

	@PostMapping("/users/resetpassword/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
			@Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users");

		return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully",
				additionalData);
	}

	@PostMapping("/users/togglestatus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@RequestBody CoreUserToggleDTO toggleStatusDTO) {
		boolean isActive = service.toggleUserStatus(toggleStatusDTO.getUserId(), toggleStatusDTO.getUserStatus());
		return buildResponse(isActive ? "User activated successfully" : "User deactivated successfully",
				Map.of("active", isActive ? "Y" : "N"));
	}

	@GetMapping("/users/resetpassword/{id}")
	public String resetPassword(@PathVariable("id") Long id, Model model) {
		CoreUserDTO user = service.findById(id); // Use the service instance
		model.addAttribute("userDTO", user);
		return "fragments/admin/users/reset_password"; // View for updating user details
	}
}
