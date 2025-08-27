package com.dms.kalari.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.dto.CoreUserDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dms.kalari.admin.repository.CoreUserRepository;

@Controller
@RequestMapping("/admin/officials")
public class OfficialController extends BaseController<CoreUserDTO, CoreUserService> {

	private final MisDesignationService designationService;
	private final NodeService nodeService;
	private final CoreUserRepository coreUserRepository; // Add this
	private final CoreUserMapper coreUserMapper; // Add this

	// Inject both services via constructor
	public OfficialController(CoreUserService coreUserService, MisDesignationService designationService,
			CoreUserRepository coreUserRepository, CoreUserMapper coreUserMapper, NodeService nodeService) {
		super(coreUserService);
		this.designationService = designationService; // Properly assign the designationService
		this.nodeService = nodeService;
		this.coreUserRepository = coreUserRepository;
		this.coreUserMapper = coreUserMapper;
	}

	@GetMapping("/")
	public String listOfficials(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
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

		return "fragments/manage/officials/list";
	}

	@GetMapping({ "/bynode/", "/bynode/{id}" })
	public String listOfficialsByNode(@PathVariable(value = "id", required = false) Long nodeId, HttpSession session,
			Model model, Authentication authentication) {
		
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		
		if(principal.getInstId() !=nodeId) // Assume we hide parent hierarchy
			model.addAttribute("isChild", true); 
		else 
			model.addAttribute("isChild", false);
		
        if(nodeId==null) {
        	nodeId = principal.getInstId();
        }	

		NodeDTO node = nodeService.findById(nodeId);
		Node.Type nodeType = node.getNodeType();
		
		

		// Get users (officials)
		List<CoreUser> users = service.listUsersByNodeAndType(nodeId, UserType.OFFICIAL);
		
		logInfo("nodeType: {}", nodeType);

		model.addAttribute("nodeName", node.getNodeName());
		model.addAttribute("nodeType", nodeType.name());
		model.addAttribute("parentId", nodeId);
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");

		return "fragments/manage/officials/bynode";
	}

	@GetMapping("/details/{id}")
	public String viewUserById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(id));
		model.addAttribute("pageTitle", "Official Detail ");
		return "fragments/manage/officials/view";
	}

	@GetMapping("/add/{id}")
	public String showAddOfficialForm(@PathVariable(value = "id") Long nodeId, Model model) {

		model.addAttribute("user", new OfficialAddDTO());

		if (nodeId != null) {

			List<DesignationDTO> designations = null;

			model.addAttribute("pageTitle", "Add Officials");
			designations = designationService.findAllByType((short) 1);

			model.addAttribute("nodeId", nodeId);

			model.addAttribute("designations", designations);

		}
		return "fragments/manage/officials/add";
	}

	@PostMapping("/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addOfficial(@PathVariable(value = "id") Long nodeId,
			@Valid @ModelAttribute OfficialAddDTO CoreUserMemberDTO, BindingResult result,
			HttpServletRequest request) {

		logInfo("Request Parameters – CoreUserMemberDTO: {}", CoreUserMemberDTO);

		if (nodeId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "ParentId not found in session"));
		}

		logInfo("Request Parameters – setUserNodeId-parentId: {}", nodeId);

		CoreUserMemberDTO.setUserNode(nodeId);
		CoreUserMemberDTO.setUserType(CoreUser.UserType.OFFICIAL);
		CoreUserMemberDTO.setUserPassword("123456");
		CoreUserMemberDTO.setUserUname("uname");

		Map<String, Object> additionalData = new HashMap<>();

		additionalData.put("loadnext", "officials_bynode/" + nodeId);

		additionalData.put("target", "users_target");
		return handleRequest(result, () -> service.saveMamber(CoreUserMemberDTO), "Official added successfully",
				additionalData);
	}

	@GetMapping("/edit/{id}")
	public String editOfficialForm(@PathVariable Long id, Model model, HttpSession session) {
		// Get the user entity
		CoreUser user = coreUserRepository.findByIdAndNotDeleted(id)
				.orElseThrow(() -> new ResourceNotFoundException("CoreUser", id));

		// Convert to CoreUserUpdateMemberDTO
		OfficialUpdateDTO userDTO = coreUserMapper.toUpdateMemberDTO(user);

		model.addAttribute("user", userDTO);
		List<DesignationDTO> designations = null;

		model.addAttribute("pageTitle", "Edit Officials");
		designations = designationService.findAllByType((short) 1);
		
		model.addAttribute("designations", designations);
		return "fragments/manage/officials/edit";
	}

	@PostMapping("/edit/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> editOfficial(@PathVariable("refId") Long userId,
			@Valid @ModelAttribute OfficialUpdateDTO coreUserUpdateDTO, BindingResult result,
			HttpSession session) {
		Map<String, Object> additionalData = new HashMap<>();
		
		CoreUserDTO user = service.findById(userId);

		additionalData.put("loadnext", "officials_bynode/" + user.getUserNode());

		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.updateMember(userId, coreUserUpdateDTO), "Details updated successfully",
				additionalData);
	}

}
