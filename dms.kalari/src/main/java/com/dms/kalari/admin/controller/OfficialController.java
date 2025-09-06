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
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.security.CustomUserPrincipal;
import com.dms.kalari.util.XorMaskHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dms.kalari.admin.repository.CoreUserRepository;

@Controller
@RequestMapping("/admin/officials")
public class OfficialController extends BaseController<CoreUserDTO, CoreUserService> {

	private final MisDesignationService designationService;
	private final NodeService nodeService;
	private final CoreUserRepository coreUserRepository; // Add this
	private final CoreUserMapper coreUserMapper; // Add this
	private final NodeRepository nodeRepository;

	// Inject both services via constructor
	public OfficialController(CoreUserService coreUserService, MisDesignationService designationService,
			CoreUserRepository coreUserRepository, CoreUserMapper coreUserMapper, NodeService nodeService,NodeRepository nodeRepository) {
		super(coreUserService);
		this.designationService = designationService; // Properly assign the designationService
		this.nodeService = nodeService;
		this.coreUserRepository = coreUserRepository;
		this.coreUserMapper = coreUserMapper;
		this.nodeRepository = nodeRepository;
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
	public String listOfficialsByNode(@PathVariable(value = "id", required = false) Long nodeId,  
			Model model, Authentication authentication) {
		
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		
		if (nodeId == null) {
			nodeId = principal.getInstId();
		}
		else {
			nodeId = XorMaskHelper.unmask(nodeId);
		}

		if (principal.getInstId() != nodeId)
			model.addAttribute("isChild", true);
		else
			model.addAttribute("isChild", false);	

		NodeDTO node = nodeService.findById(nodeId);
		Node.Type nodeType = node.getNodeType();
		
		

		// Get users (officials)
		List<CoreUser> users = service.listUsersByNodeAndType(nodeId, UserType.OFFICIAL);
		
		logInfo("nodeType: {}", nodeType);

		model.addAttribute("nodeName", node.getNodeName());
		model.addAttribute("nodeType", nodeType.name());
        model.addAttribute("parentId", XorMaskHelper.mask(nodeId));
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");

		return "fragments/manage/officials/bynode";
	}

	@GetMapping("/details/{id}")
	public String viewUserById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(XorMaskHelper.unmask(id)));
		model.addAttribute("pageTitle", "Official Detail ");
		return "fragments/manage/officials/view";
	}

	@GetMapping("/add/{id}")
	public String showAddOfficialForm(@PathVariable(value = "id") Long mNodeId, Model model) {

		model.addAttribute("user", new OfficialAddDTO());
        Long nodeId = XorMaskHelper.unmask(mNodeId);

		if (nodeId != null) {
		
			
			NodeDTO node = nodeService.findById(nodeId);
			Node.Type nodeType = node.getNodeType();

			model.addAttribute("pageTitle", "Add Officials");
			List<DesignationDTO> designations = designationService.findAllByDesigNodeLevelAndType(nodeType, (short) 1 );
			designations = designations.stream()
			        .filter(dto -> dto.getDesigLevel() != null)
			        .map(dto -> new DesignationDTO(
			            XorMaskHelper.mask(dto.getDesigId()),  // mask id
			            dto.getDesigName(),
			            dto.getDesigLevel()
			        ))
			        .collect(Collectors.toList());  // keep it as List

			model.addAttribute("designations", designations);

			model.addAttribute("nodeId", mNodeId);

			model.addAttribute("designations", designations);

		}
		return "fragments/manage/officials/add";
	}

	@PostMapping("/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addOfficial(@PathVariable(value = "id") Long mNodeId,
			@Valid @ModelAttribute OfficialAddDTO CoreUserMemberDTO, BindingResult result,
			Authentication authentication,
			HttpServletRequest request) {

		Long nodeId = XorMaskHelper.unmask(mNodeId);

		CoreUserMemberDTO.setUserNode(nodeId);
		CoreUserMemberDTO.setUserType(CoreUser.UserType.OFFICIAL);
		CoreUserMemberDTO.setUserPassword("123456");
		CoreUserMemberDTO.setUserUname("uname");
		
		//Check tamper with the masked ID in the form submission and inject a designation that isn’t valid for that node level.
	    NodeDTO node = nodeService.findById(nodeId);
	    Node.Type nodeType = node.getNodeType();
	    
	    
		if (CoreUserMemberDTO.getUserDesig() != null) {
			Long unmaskedId = XorMaskHelper.unmask(CoreUserMemberDTO.getUserDesig());
			List<Long> validDesigIds = designationService.findAllByDesigNodeLevel(nodeType).stream()
					.map(DesignationDTO::getDesigId).collect(Collectors.toList());
			if (!validDesigIds.contains(unmaskedId)) {
				throw new IllegalArgumentException("Invalid designation submitted!");
			}
			CoreUserMemberDTO.setUserDesig(unmaskedId);
		}
	    		
	    //Validate allowed node IDs
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
	    List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds( principal.getInstId());
	    if (!allowedNodeIds.contains(node.getNodeId())) {
	        throw new SecurityException("Invalid node submitted!");
	    }
		

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "officials_bynode/"  + mNodeId);

		additionalData.put("target", "users_target");
		return handleRequest(result, () -> service.saveMamber(CoreUserMemberDTO), "Official added successfully",
				additionalData);
	}

	@GetMapping("/edit/{id}")
	public String editOfficialForm(@PathVariable Long id, Model model) {
		
		Long userId = XorMaskHelper.unmask(id);
		// Get the user entity
		CoreUser user = coreUserRepository.findByIdAndNotDeleted(userId)
				.orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
		
		

		// Convert to CoreUserUpdateMemberDTO
		OfficialUpdateDTO userDTO = coreUserMapper.toUpdateMemberDTO(user);
		userDTO.setUserDesig(XorMaskHelper.mask(userDTO.getUserDesig()));

		model.addAttribute("user", userDTO);

		model.addAttribute("userId", id);
		model.addAttribute("pageTitle", "Edit Officials");
		NodeDTO node = nodeService.findById(userDTO.getUserNode());
		Node.Type nodeType = node.getNodeType();
		//designations = designationService.findAllByDesigNodeLevel(nodeType);
		
		List<DesignationDTO> designations = designationService.findAllByDesigNodeLevelAndType(nodeType, (short) 1 );

		designations = designations.stream()
		        .filter(dto -> dto.getDesigLevel() != null)
		        .map(dto -> new DesignationDTO(
		            XorMaskHelper.mask(dto.getDesigId()),  // mask id
		            dto.getDesigName(),
		            dto.getDesigLevel()
		        ))
		        .collect(Collectors.toList());  // keep it as List

		model.addAttribute("designations", designations);
		
		return "fragments/manage/officials/edit";
	}

	@PostMapping("/edit/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> editOfficial(@PathVariable("refId") Long id,
			@Valid @ModelAttribute OfficialUpdateDTO coreUserUpdateDTO, BindingResult result,
			Authentication authentication,
			HttpSession session) {
		Map<String, Object> additionalData = new HashMap<>();
		
		Long userId = XorMaskHelper.unmask(id);
		
		CoreUserDTO user = service.findById(userId);
			
	    NodeDTO node = nodeService.findById(user.getUserNode());
	    Node.Type nodeType = node.getNodeType();
	    
	    
		if (coreUserUpdateDTO.getUserDesig() != null) {
			Long unmaskedId = XorMaskHelper.unmask(coreUserUpdateDTO.getUserDesig());
			List<Long> validDesigIds = designationService.findAllByDesigNodeLevel(nodeType).stream()
					.map(DesignationDTO::getDesigId).collect(Collectors.toList());
			if (!validDesigIds.contains(unmaskedId)) {
				throw new IllegalArgumentException("Invalid designation submitted!");
			}
			coreUserUpdateDTO.setUserDesig(unmaskedId);
		}
	    
	    //Validate allowed node IDs
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
	    List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds( principal.getInstId());
	    if (!allowedNodeIds.contains(node.getNodeId())) {
	        throw new SecurityException("Invalid node submitted!");
	    }

		additionalData.put("loadnext", "officials_bynode/" + XorMaskHelper.mask(user.getUserNode()));

		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.updateMember(userId, coreUserUpdateDTO), "Details updated successfully",
				additionalData);
	}

}
