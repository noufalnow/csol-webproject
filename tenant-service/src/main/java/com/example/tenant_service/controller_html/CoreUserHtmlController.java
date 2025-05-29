package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.CoreUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserToggleDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.dto.users.UserMemberDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.entity.CoreUser.UserType;
import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.service.MisDesignationService;
import com.example.tenant_service.service.NodeService;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class CoreUserHtmlController extends BaseController<CoreUserDTO, CoreUserService> {

	private final MisDesignationService designationService;
	private final NodeService nodeService;

	// Inject both services via constructor
	public CoreUserHtmlController(CoreUserService coreUserService, MisDesignationService designationService,
			NodeService nodeService) {
		super(coreUserService);
		this.designationService = designationService; // Properly assign the designationService
		this.nodeService = nodeService;
	}

	@GetMapping("/html")
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
		model.addAttribute("pageTitle", "User List - My Application");
		model.addAttribute("pageUrl", "/users/html");

		return "fragments/core_user_list";
	}

	@GetMapping({ "/html/bynode", "/html/bynode/{id}" }) // Supports both patterns
	public String listUsersByNode(@PathVariable(value = "id", required = false) Long nodeId, HttpSession session,
			Model model) {

		// HttpSession session = request.getSession();
		if (nodeId != null) {
			session.setAttribute("ParentId", nodeId); // for data entry
		} else {
			nodeId = (Long) session.getAttribute("NODE_ID");
		}

		NodeDTO node = nodeService.findById(nodeId);

		Node.Type nodeType = (Node.Type) node.getNodeType();
		Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");
		
		if (userNodeType == Node.Type.KALARI && nodeType == Node.Type.KALARI) {
			model.addAttribute("allowAddMember", true);
		}  
		
		if (nodeType == Node.Type.KALARI) {
			model.addAttribute("showMemberPanel", true);
		}
		model.addAttribute("pageTitle", "Members");
		

		List<CoreUser> users = service.listUsersByNodeAndType(nodeId,UserType.MEMBER);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");
		return "fragments/node_users";
	}
	
	
	@GetMapping({ "/html/bynodeoff", "/html/bynodeoff/{id}" }) // Supports both patterns
	public String listUsersByNodeOfficial(@PathVariable(value = "id", required = false) Long nodeId, HttpSession session,
			Model model) {

		// HttpSession session = request.getSession();
		if (nodeId != null) {
			session.setAttribute("ParentId", nodeId); // for data entry
		} else {
			nodeId = (Long) session.getAttribute("NODE_ID");
		}

		NodeDTO node = nodeService.findById(nodeId);

		Node.Type nodeType = (Node.Type) node.getNodeType();
		Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");
		
		if (nodeType.getLevel() > userNodeType.getLevel() && (userNodeType != Node.Type.KALARI)) {
			model.addAttribute("allowAddMember", true); /* official*/
		} 
		model.addAttribute("showMemberPanel", true);
		
		model.addAttribute("pageTitle", "Officials");

		List<CoreUser> users = service.listUsersByNodeAndType(nodeId,UserType.OFFICIAL);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");
		return "fragments/node_users";
	}

	@GetMapping("/html/bynodeglobal")
	public String listUsersByNodeBlobal(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long nodeId = (Long) session.getAttribute("NODE_ID");

		List<CoreUser> users = service.listUsersByNode(nodeId);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");
		return "fragments/node_users";
	}

	@GetMapping("/html/{id}")
	public String viewUserById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(id));
		model.addAttribute("pageTitle", "User Detail - My Application");
		return "fragments/core_user_detail";
	}

	@GetMapping("/html/view/{id}")
	public String viewUserBiewById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(id));
		model.addAttribute("pageTitle", "User Detail - My Application");
		return "fragments/profile/view";
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

	@GetMapping("/html/addmember")
	public String showAddMemberUserForm(Model model, HttpServletRequest request) {
		model.addAttribute("pageTitle", "Add New Official - ##");
		model.addAttribute("user", new UserMemberDTO());
		// Fetch designations from the service and pass them to the model
		List<DesignationDTO> designations = designationService.findAll();
		model.addAttribute("designations", designations);

		/*HttpSession session = request.getSession(false);
		System.out.println("=== Session Attributes ===");
		Collections.list(session.getAttributeNames())
				.forEach(name -> System.out.println(name + " = " + session.getAttribute(name)));*/

		return "fragments/add_memberuser";
	}

	@PostMapping("/html/addmember")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addMember(@Valid @ModelAttribute UserMemberDTO userMemberDTO,
			BindingResult result, HttpServletRequest request) {

		logInfo("Request Parameters – userMemberDTO: {}", userMemberDTO);

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

		userMemberDTO.setUserNode(parentId);

		if (!Node.Type.KALARI.equals(session.getAttribute("NODE_TYPE"))) {
			userMemberDTO.setUserType(CoreUser.UserType.OFFICIAL);
		}

		/*System.out.println("=== Session Attributes ===");
		Collections.list(session.getAttributeNames())
				.forEach(name -> System.out.println(name + " = " + session.getAttribute(name)));*/

		userMemberDTO.setUserPassword("123456");

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "users/html/bynode/" + parentId);
		additionalData.put("target", "users_target");
		return handleRequest(result, () -> service.saveMamber(userMemberDTO), "User added successfully",
				additionalData);
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

		return handleRequest(result, () -> service.updateUser(userId, coreUserUpdateDTO), "User updated successfully",
				additionalData);
	}

	@PostMapping("/html/resetPassword/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("refId") Long refId,
			@Valid @ModelAttribute CoreUserPasswordDTO passwordDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/users/html");

		return handleRequest(result, () -> service.resetPassword(refId, passwordDTO), "Password reset successfully",
				additionalData);
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
