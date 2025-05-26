package com.example.tenant_service.controller_html;

import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.MemberEventDTO;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.service.MemberEventService;
import com.example.tenant_service.service.NodeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member-events")
public class MemberEventHtmlController extends BaseController<MemberEventDTO, MemberEventService> {

	public MemberEventHtmlController(MemberEventService memberEventService) {
		super(memberEventService);
	}

	/*
	 * @GetMapping("/{id}") public String getById(@PathVariable Long id, Model
	 * model) { model.addAttribute("event", service.findById(id)); return
	 * "fragments/memevent/details"; }
	 */

	@GetMapping("/member/{memberId}")
	public String listByMember(@PathVariable Long memberId, Model model) {
		List<MemberEventDTO> events = service.findByMember(memberId);
		model.addAttribute("events", events);
		return "fragments/memevent/list-by-member";
	}

	@GetMapping("/node/{nodeId}")
	public String listByNode(@PathVariable Long nodeId, Model model) {
		List<MemberEventDTO> events = service.findByNode(nodeId);
		model.addAttribute("events", events);
		return "fragments/memevent/list-by-node";
	}

	@GetMapping("/event/{eventId}")
	public String listByEvent(@PathVariable Long eventId, Model model) {
		List<MemberEventDTO> events = service.findByEvent(eventId);
		model.addAttribute("events", events);
		return "fragments/memevent/list-by-event";
	}

	@GetMapping("/pending-approvals")
	public String pendingApprovals(Model model) {
		model.addAttribute("events", service.getPendingApprovals());
		return "fragments/memevent/approval-list";
	}

	@GetMapping("/html/add")
	public String showAddMemberEventForm(@RequestParam(name = "eventId", required = false) Long eventId, Model model) {

		model.addAttribute("pageTitle", "Add Member to Event");
		model.addAttribute("memberEvent", new MemberEventDTO());
		model.addAttribute("eventId", eventId);
		return "fragments/profile/add_member_event";
	}

	@PostMapping("/html/add/{eventId}")
	public ResponseEntity<Map<String, Object>> addMemberToEvent(@PathVariable Long eventId,
			@ModelAttribute MemberEventDTO memberEventDTO, BindingResult result,HttpServletRequest request) {
		
		
		Map<String, Object> additionalData = new HashMap<>();
		if (memberEventDTO.items == null) {
		    result.reject("items", "Items selection cannot be null.");
		    return handleRequest(result, null, "Validation failed", additionalData);
		}
			

		if (memberEventDTO.items.isEmpty()) {
		    logInfo("Validation failed: No items selected.");
		    result.reject("items", "You must select at least one item.");
		    return handleRequest(result, null, "Validation failed", additionalData);
		}
		
		memberEventDTO.setApplyDate(LocalDateTime.now());
						
 	    HttpSession session = request.getSession(false);
		memberEventDTO.setMemberId((Long) session.getAttribute("USER_ID"));
		memberEventDTO.setNodeId((Long) session.getAttribute("ParentId"));

		
		//additionalData.put("loadnext", "/nodes/html");

		return handleRequest(result, () -> service.save(memberEventDTO), "Applied successfully", additionalData);
	}

	/*
	 * @PostMapping("/html/add")
	 * 
	 * @ResponseBody public ResponseEntity<Map<String, Object>> addNode(
	 * 
	 * @Valid @ModelAttribute MemberEventDTO memberEventDTO, BindingResult result) {
	 * 
	 * Map<String, Object> additionalData = new HashMap<>();
	 * additionalData.put("loadnext", "/nodes/html");
	 * 
	 * return handleRequest(result, () -> service.save(memberEventDTO),
	 * "Node added successfully", additionalData); }
	 */

	@PostMapping("/{id}/approve")
	public String approveApplication(@PathVariable Long id, @RequestParam Long approvedBy) {
		service.approveApplication(id, approvedBy);
		return "redirect:/member-events/pending-approvals";
	}

	@GetMapping("/pending-result-approvals")
	public String pendingResultApprovals(Model model) {
		model.addAttribute("events", service.getPendingResultApprovals());
		return "fragments/memevent/result-approval-list";
	}

	@PostMapping("/{id}/enter-result")
	public String enterResult(@PathVariable Long id, @RequestParam Map<Integer, String> items,
			@RequestParam Long enteredBy) {
		service.enterResult(id, items, enteredBy);
		return "redirect:/member-events/" + id;
	}

	@PostMapping("/{id}/approve-result")
	public String approveResult(@PathVariable Long id, @RequestParam Long approvedBy) {
		service.approveResult(id, approvedBy);
		return "redirect:/member-events/pending-result-approvals";
	}
}