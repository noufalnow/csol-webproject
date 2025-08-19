package com.dms.kalari.controller;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.service.CoreUserService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.EventDTO;
import com.dms.kalari.dto.EventItemDTO;
import com.dms.kalari.dto.MemberEventDTO;
import com.dms.kalari.dto.NodeDTO;
import com.dms.kalari.entity.EventItem;
import com.dms.kalari.entity.EventItemMap;
import com.dms.kalari.repository.EventItemRepository;
import com.dms.kalari.service.EventItemService;
import com.dms.kalari.service.EventService;
import com.dms.kalari.service.MemberEventService;
import com.dms.kalari.service.NodeService;
import com.dms.kalari.service.PdfGenerationService;
import com.lowagie.text.pdf.AcroFields.Item;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member-events")
public class MemberEventHtmlController extends BaseController<MemberEventDTO, MemberEventService> {

	private final EventService eventService;
	private final PdfGenerationService pdfGenerationService;
	private final NodeService nodeService;
	private final CoreUserService coreUserService;
	private final EventItemService eventItemService;
	private final EventItemRepository eventItemRepository;
	

	public MemberEventHtmlController(CoreUserService coreUserService, MemberEventService memberEventService,
			EventService eventService, PdfGenerationService pdfGenerationService, NodeService nodeService,EventItemService eventItemService, EventItemRepository eventItemRepository) {
		super(memberEventService);
		this.eventService = eventService;
		this.pdfGenerationService = pdfGenerationService;
		this.nodeService = nodeService;
		this.coreUserService = coreUserService;
		this.eventItemService = eventItemService;
		this.eventItemRepository = eventItemRepository;

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

	@GetMapping("/html/add") // apply event by user
	public String showAddMemberEventForm(@RequestParam(name = "eventId", required = false) Long eventId, Model model, HttpServletRequest request) {
		
	    HttpSession session = request.getSession(false);
	    Long userId = session != null ? (Long) session.getAttribute("USER_ID") : null;
	    
	    CoreUserDTO user = coreUserService.findById(userId);
	    EventItemMap.Category category = 
	        user.getUserMemberCategory() == CoreUser.MemberCategory.JUNIOR
	            ? EventItemMap.Category.JUNIOR
	            : EventItemMap.Category.SENIOR;

	    EventDTO event = eventService.findByIdWithItemsByCategory(eventId, category);

	    List<EventItemDTO> eventItems = category == EventItemMap.Category.JUNIOR
	        ? event.getJuniorEventItems()
	        : event.getSeniorEventItems();


	    
	    logInfo("eventItems: {}",eventItems);


	    model.addAttribute("eventItems", eventItems);

		model.addAttribute("pageTitle", "Add Member to Event");
		model.addAttribute("memberEvent", new MemberEventDTO());
		model.addAttribute("eventId", eventId);
		return "fragments/profile/add_member_event";
	}

	@PostMapping("/html/add/{eventId}")
	public ResponseEntity<Map<String, Object>> addMemberToEvent(@PathVariable Long eventId,
			@ModelAttribute MemberEventDTO memberEventDTO, BindingResult result, HttpServletRequest request) {

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

		EventDTO eventDTO = eventService.findById(memberEventDTO.getEventId());

		HttpSession session = request.getSession(false);
		memberEventDTO.setMemberId((Long) session.getAttribute("USER_ID"));
		memberEventDTO.setMemberNodeId((Long) session.getAttribute("NODE_ID"));
		memberEventDTO.setNodeId((Long) eventDTO.getEventHostId());

		additionalData.put("loadnext", "reload");

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

	@GetMapping("/html/approve/{meId}")
	public ResponseEntity<Map<String, Object>> approveApplication(@PathVariable Long meId,
			// BindingResult result, // Must come right after the validated object (if any)
			HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long approvedBy = session != null ? (Long) session.getAttribute("USER_ID") : null;

		MemberEventDTO memberEvent = service.findById(meId);

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "events/html/listparticipants?eventId=" + memberEvent.getEventId());
		additionalData.put("target", "modal");
		service.approveApplication(meId, approvedBy);

		return buildResponse("Approved successfully", additionalData);
	}

	@GetMapping("/html/score/{meId}")
	public String showScoreEntryForm(@PathVariable Long meId, Model model) {
		MemberEventDTO participant = service.findById(meId);
		
		Long eventId = participant.getEventId();

	    List<EventItemDTO> allItems = eventItemService.findAll(); // or eventService.getItemsByEventId(eventId)

	    Map<Long, String> itemIdToName = allItems.stream()
	        .collect(Collectors.toMap(EventItemDTO::getEvitemId, EventItemDTO::getEvitemName));
	    
	    model.addAttribute("itemNameMap", itemIdToName);


		// Ensure items map exists
		if (participant.getItems() == null) {
			participant.setItems(new HashMap<>());
		}

		model.addAttribute("participant", participant);
		
		return "fragments/events/score_entry";
	}

	@PostMapping("/html/score/{meId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveScores(@PathVariable Long meId,
			@ModelAttribute MemberEventDTO participantUpdate, BindingResult result, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long resultEntryBy = session != null ? (Long) session.getAttribute("userId") : null;

		Map<String, Object> additionalData = new HashMap<>();

		try {
			MemberEventDTO existing = service.findById(meId);
			if (existing != null) {
				Map<Integer, String> existingItems = existing.getItems();
				if (existingItems == null) {
					existingItems = new HashMap<>();
				}

				Map<Integer, String> newItems = participantUpdate.getItems();
				if (newItems != null && !newItems.isEmpty()) {
					// Snapshot of keys before update
					Set<Integer> previousKeys = new HashSet<>(existingItems.keySet());

					// Apply new values (update or insert)
					for (Map.Entry<Integer, String> entry : newItems.entrySet()) {
						existingItems.put(entry.getKey(), entry.getValue());
					}

					// Set missing keys (not in new submission) to "P"
					for (Integer key : previousKeys) {
						if (!newItems.containsKey(key)) {
							existingItems.put(key, "P");
						}
					}

					existing.setResultDate(LocalDateTime.now());
				}

				existing.setItems(existingItems);
				existing.setResultEntryBy(resultEntryBy);
			}

			additionalData.put("loadnext", "/events/html/listparticipants?eventId=" + existing.getEventId());
			additionalData.put("target", "modal");

			return handleRequest(result, () -> service.save(existing), "Scores saved successfully", additionalData);
		} catch (Exception e) {
			return handleRequest(result, null, "Error saving scores: " + e.getMessage(), additionalData);
		}

	}

	@GetMapping("/html/certificate/{meId}")
	public ResponseEntity<byte[]> generateCertificate(@PathVariable Long meId,
			@RequestParam(required = false) Integer itemId) throws Exception {

		MemberEventDTO participant = service.findById(meId);
		EventDTO event = eventService.findById(participant.getEventId());
		NodeDTO node = nodeService.findNodeById(event.getEventHostId());

		Map<Integer, String> allItems = participant.getItems();
		Map<Integer, String> filteredItems = itemId != null && allItems != null && allItems.containsKey(itemId)
				? Map.of(itemId, allItems.get(itemId))
				: allItems;

		Map<String, List<String>> medalItems = organizeItemsByMedal(filteredItems);

		Map<String, Object> data = new HashMap<>();
		data.put("participant", participant);
		data.put("eventName", event.getEventName());
		data.put("meEvent", meId);
		data.put("itemId", itemId);
		data.put("hostName", node.getNodeName());
		data.put("resultDate", participant.getResultDate() != null ? participant.getResultDate() : LocalDateTime.now());
		data.put("goldItems", medalItems.get("gold"));
		data.put("silverItems", medalItems.get("silver"));
		data.put("bronzeItems", medalItems.get("bronze"));
		data.put("participationItems", medalItems.get("participation"));

		// Long meId = (Long) data.get("meEvemt");
		// Integer itemId = (Integer) data.get("itemId");

		String verificationId = itemId != null ? meId + "-" + itemId : meId.toString();
		data.put("verificationId", verificationId);

		byte[] pdfBytes = pdfGenerationService.generateMultiPageCertificate(data);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "certificate_" + participant.getMemberName() + ".pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	@GetMapping("/html/certificate2/{meId}")
	public String previewCertificate(@PathVariable Long meId, Model model) throws Exception {
		MemberEventDTO participant = service.findById(meId);
		EventDTO event = eventService.findById(participant.getEventId());
		NodeDTO node = nodeService.findNodeById(event.getEventHostId());

		Map<String, List<String>> medalItems = organizeItemsByMedal(participant.getItems());

		model.addAttribute("participant", participant);
		model.addAttribute("eventName", event.getEventName());
		model.addAttribute("hostName", node.getNodeName());
		model.addAttribute("resultDate",
				participant.getResultDate() != null ? participant.getResultDate() : LocalDateTime.now());
		model.addAttribute("goldItems", medalItems.get("gold"));
		model.addAttribute("silverItems", medalItems.get("silver"));
		model.addAttribute("bronzeItems", medalItems.get("bronze"));
		model.addAttribute("participationItems", medalItems.get("participation"));

		return "fragments/events/certificate-multi"; // Returns the Thymeleaf template as HTML
	}

	private Map<String, List<String>> organizeItemsByMedal(Map<Integer, String> items) {
		Map<String, List<String>> medalItems = new HashMap<>();
		medalItems.put("gold", new ArrayList<>());
		medalItems.put("silver", new ArrayList<>());
		medalItems.put("bronze", new ArrayList<>());
		medalItems.put("participation", new ArrayList<>());

		if (items != null) {
			for (Entry<Integer, String> entry : items.entrySet()) {
			    String itemName = eventItemRepository.findByIdAndNotDeleted(entry.getKey().longValue())
				        .map(EventItem::getEvitemName)
				        .orElse("Unknown Item");

				switch (entry.getValue()) {
				case "A":
					medalItems.get("gold").add(itemName);
					break;
				case "B":
					medalItems.get("silver").add(itemName);
					break;
				case "C":
					medalItems.get("bronze").add(itemName);
					break;
				case "P":
				default:
					medalItems.get("participation").add(itemName);
					break;
				}
			}
		}

		return medalItems;
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