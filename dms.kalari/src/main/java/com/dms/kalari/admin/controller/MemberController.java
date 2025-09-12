package com.dms.kalari.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.dto.MemberAddDTO;
import com.dms.kalari.admin.dto.MemberUpdateDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.mapper.CoreUserMapper;
import com.dms.kalari.admin.service.MemberUserService;
import com.dms.kalari.admin.service.MisDesignationService;
import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.exception.ResourceNotFoundException;
import com.dms.kalari.security.CustomUserPrincipal;
import com.dms.kalari.util.XorMaskHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dms.kalari.admin.repository.CoreUserRepository;

@Controller
@RequestMapping("/admin/members")
public class MemberController extends BaseController<MemberAddDTO, MemberUserService> {

	private final MisDesignationService designationService;
	private final NodeService nodeService;
	private final CoreUserRepository coreUserRepository;
	private final CoreUserMapper coreUserMapper;
	private final NodeRepository nodeRepository;

	public MemberController(MemberUserService memberUserService, MisDesignationService designationService,
			CoreUserRepository coreUserRepository, CoreUserMapper coreUserMapper, NodeService nodeService,
			NodeRepository nodeRepository) {
		super(memberUserService);
		this.designationService = designationService;
		this.nodeService = nodeService;
		this.coreUserRepository = coreUserRepository;
		this.coreUserMapper = coreUserMapper;
		this.nodeRepository = nodeRepository;
	}

	@GetMapping("/")
	public String listMembers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortField, @RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(required = false) String search, Model model) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}", page, size,
				sortField, sortDir, search);

		Page<MemberAddDTO> userPage = service.findAllPaginate(pageable, search);

		logInfo("User Page - Total Elements: {}, Total Pages: {}", userPage.getTotalElements(),
				userPage.getTotalPages());
		logInfo("Users: {}", userPage.getContent());

		setupPagination(model, userPage, sortField, sortDir);

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "Member List ");
		model.addAttribute("pageUrl", "/members");

		return "fragments/manage/members/list";
	}

	@GetMapping({ "/bynode/", "/bynode/{id}" })
	public String listMembersByNode(@PathVariable(value = "id", required = false) Long nodeId, Model model,
			Authentication authentication) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

		if (nodeId == null) {
			nodeId = principal.getInstId();
		} else {
			nodeId = XorMaskHelper.unmask(nodeId);
		}

		if (principal.getInstId() != nodeId)
			model.addAttribute("isChild", true);
		else
			model.addAttribute("isChild", false);

		NodeDTO node = nodeService.findById(nodeId);
		Node.Type nodeType = node.getNodeType();

		List<CoreUser> users = service.listUsersByNodeAndType(nodeId, UserType.MEMBER);

		logInfo("nodeType: {}", nodeType);

		model.addAttribute("nodeName", node.getNodeName());
		model.addAttribute("nodeType", nodeType.name());
		model.addAttribute("parentId", XorMaskHelper.mask(nodeId));
		model.addAttribute("users", users);
		model.addAttribute("target", "users_target");

		return "fragments/manage/members/bynode";
	}

	@GetMapping("/details/{id}")
	public String viewMemberById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(XorMaskHelper.unmask(id)));
		
		
		model.addAttribute("pageTitle", "Participant's Detail ");
		return "fragments/manage/members/view";
	}

	@GetMapping("/profile/view/{id}")
	public String viewMemberProfileById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.findById(XorMaskHelper.unmask(id)));
		model.addAttribute("pageTitle", "Participant's Profile ");
		return "fragments/admin/users/profile/view";
	}

	@GetMapping("/add/{id}")
	public String showAddMemberForm(@PathVariable(value = "id") Long mNodeId, Model model) {
		model.addAttribute("user", new MemberAddDTO()); // ← use MemberAddDTO
		Long nodeId = XorMaskHelper.unmask(mNodeId);

		if (nodeId != null) {

			model.addAttribute("pageTitle", "Add Participants");
			NodeDTO node = nodeService.findById(nodeId);
			Node.Type nodeType = node.getNodeType();
			List<DesignationDTO> designations = designationService.findAllByDesigNodeLevelAndType(nodeType, (short) 2 );
			designations = designations.stream().filter(dto -> dto.getDesigLevel() != null)
					.map(dto -> new DesignationDTO(XorMaskHelper.mask(dto.getDesigId()), // mask id
							dto.getDesigName(), dto.getDesigLevel()))
					.collect(Collectors.toList()); // keep it as List

			model.addAttribute("nodeId", mNodeId);
			model.addAttribute("designations", designations);
		}
		return "fragments/manage/members/add";
	}

	@PostMapping("/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addMember(@PathVariable(value = "id") Long mNodeId,
			@Valid @ModelAttribute MemberAddDTO memberAddDTO, BindingResult result, Authentication authentication,
			HttpServletRequest request) {

		logInfo("Request Parameters – memberAddDTO: {}", memberAddDTO);

		Long nodeId = XorMaskHelper.unmask(mNodeId);

		//nodeId = XorMaskHelper.unmask(mNodeId);

		logInfo("Request Parameters – setUserNodeId-parentId: {}", nodeId);

		memberAddDTO.setUserNode(nodeId);
		memberAddDTO.setUserType(CoreUser.UserType.MEMBER);
		memberAddDTO.setUserPassword("123456");
		memberAddDTO.setUserUname("uname");

		// Check tamper with the masked ID in the form submission and inject a
		// designation that isn’t valid for that node level.
		NodeDTO node = nodeService.findById(nodeId);
		Node.Type nodeType = node.getNodeType();

		if (memberAddDTO.getUserDesig() != null) {
			Long unmaskedId = XorMaskHelper.unmask(memberAddDTO.getUserDesig());
			List<Long> validDesigIds = designationService.findAllByDesigNodeLevel(nodeType).stream()
					.map(DesignationDTO::getDesigId).collect(Collectors.toList());
			if (!validDesigIds.contains(unmaskedId)) {
				throw new IllegalArgumentException("Invalid designation submitted!");
			}
			memberAddDTO.setUserDesig(unmaskedId);
		}

		// Validate allowed node IDs
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds(principal.getInstId());
		if (!allowedNodeIds.contains(nodeId)) {
			throw new SecurityException("Invalid node submitted!");
		}

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "members_bynode/" + mNodeId);
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.saveMamber(memberAddDTO), "Participant added successfully",
				additionalData);
	}

	@GetMapping("/edit/{id}")
	public String editMemberForm(@PathVariable Long id, Model model) {

		Long userId = XorMaskHelper.unmask(id);

		CoreUser user = coreUserRepository.findByIdAndNotDeleted(userId)
				.orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

		MemberUpdateDTO userDTO = coreUserMapper.toMemberUpdateDTO(user); // ← mapper to MemberUpdateDTO
		userDTO.setUserDesig(XorMaskHelper.mask(userDTO.getUserDesig()));

		// logInfo("User DTO: {}", userDTO);

		model.addAttribute("user", userDTO);
		model.addAttribute("userId", id);

		model.addAttribute("pageTitle", "Edit Participant's details");
		NodeDTO node = nodeService.findById(userDTO.getUserNode());
		Node.Type nodeType = node.getNodeType();
		List<DesignationDTO> designations = designationService.findAllByDesigNodeLevelAndType(nodeType, (short) 2 );
		designations = designations.stream().filter(dto -> dto.getDesigLevel() != null)
				.map(dto -> new DesignationDTO(XorMaskHelper.mask(dto.getDesigId()), // mask id
						dto.getDesigName(), dto.getDesigLevel()))
				.collect(Collectors.toList()); // keep it as List

		model.addAttribute("designations", designations);
		return "fragments/manage/members/edit";
	}

	@PostMapping("/edit/{refId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> editMember(@PathVariable("refId") Long id,
			@Valid @ModelAttribute MemberUpdateDTO coreUserUpdateDTO, BindingResult result,
			Authentication authentication, HttpSession session) {
		Map<String, Object> additionalData = new HashMap<>();

		Long userId = XorMaskHelper.unmask(id);

		MemberAddDTO user = service.findById(userId); // ← service returns MemberAddDTO
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

		// Validate allowed node IDs
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds(principal.getInstId());
		if (!allowedNodeIds.contains(node.getNodeId())) {
			throw new SecurityException("Invalid node submitted!");
		}

		additionalData.put("loadnext", "members_bynode/" + XorMaskHelper.mask(user.getUserNode()));
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.updateMember(userId, coreUserUpdateDTO),
				"Participants details updated successfully", additionalData);
	}
}
