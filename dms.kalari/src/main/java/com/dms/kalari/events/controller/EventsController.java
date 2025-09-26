package com.dms.kalari.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.service.MemberUserService;
import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.events.dto.EventDTO;
import com.dms.kalari.events.dto.EventItemDTO;
import com.dms.kalari.events.dto.MemberEventDTO;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.EventItemMap.Category;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.service.EventItemMapService;
import com.dms.kalari.events.service.EventItemService;
import com.dms.kalari.events.service.EventService;
import com.dms.kalari.events.service.MemberEventItemService;
import com.dms.kalari.events.service.MemberEventService;
import com.dms.kalari.security.CustomUserPrincipal;
import com.dms.kalari.util.XorMaskHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/champ/events")
public class EventsController extends BaseController<EventDTO, EventService> {

	private final NodeService nodeService;
	private final MemberEventService memberEventService;
	private final EventItemService eventItemService;
	private final EventItemMapService eventItemMapService;
	private final MemberUserService memberUserService;
	private final MemberEventItemService memberEventItemService;

	public EventsController(EventService eventService, NodeService nodeService, MemberEventService memberEventService,
			EventItemService eventItemService, MemberUserService memberUserService,
			EventItemMapService eventItemMapService,MemberEventItemService memberEventItemService) {
		super(eventService);
		this.nodeService = nodeService;
		this.memberEventService = memberEventService;
		this.eventItemService = eventItemService;
		this.memberUserService = memberUserService;
		this.eventItemMapService = eventItemMapService;
		this.memberEventItemService = memberEventItemService;
	}

	@GetMapping("/")
	public String listEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "eventPeriodStart") String sortField,
			@RequestParam(defaultValue = "desc") String sortDir, @RequestParam(required = false) String search,
			Model model) {

		// Construct the Pageable object
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}", page, size,
				sortField, sortDir, search);

		Page<EventDTO> eventPage = service.findAllPaginate(pageable, search);

		logInfo("Event Page - Total Elements: {}, Total Pages: {}", eventPage.getTotalElements(),
				eventPage.getTotalPages());

		// Use the reusable method for setting up pagination
		setupPagination(model, eventPage, sortField, sortDir);

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "Event List");
		model.addAttribute("pageUrl", "/events");
		model.addAttribute("today", LocalDate.now());

		return "fragments/manage/events/list";
	}

	@GetMapping({ "/bynode/", "/bynode/{id}" })
	public String listEventsByNode(@PathVariable(value = "id", required = false) Long nodeId, Model model,
			Authentication authentication, HttpSession session) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

		if (nodeId == null) {
			nodeId = principal.getInstId();
		} else {
			nodeId = XorMaskHelper.unmask(nodeId);
		}

		// Store in session for data entry
		session.setAttribute("ParentId", nodeId);

		// Check if viewing child node
		if (principal.getInstId() != nodeId) {
			model.addAttribute("isChild", true);
		} else {
			model.addAttribute("isChild", false);
		}

		NodeDTO node = nodeService.findById(nodeId);
		Node.Type nodeType = node.getNodeType();
		Node.Type userNodeType = principal.getNodeType();

		logInfo("Node Type: {}, User Node Type: {}, Selected Node: {}", nodeType.getLevel(), userNodeType.getLevel(),
				nodeId);

		// Check if user can add events (similar to officials logic)
		if (nodeType.getLevel() >= userNodeType.getLevel()) {
			model.addAttribute("allowAddEvent", true);
		} else if (userNodeType == nodeType && node.getNodeId().equals(principal.getInstId())) {
			model.addAttribute("allowAddEvent", true);
		}

		List<EventDTO> events = service.findByHostNode(nodeId);
		model.addAttribute("nodeName", node.getNodeName());
		model.addAttribute("nodeType", nodeType.name());
		model.addAttribute("parentId", XorMaskHelper.mask(nodeId));
		model.addAttribute("events", events);
		model.addAttribute("target", "events_target");

		return "fragments/events/events";
	}

	@GetMapping("/all/{id}")
	public String listAllEvents(@PathVariable(value = "id", required = false) Long nodeId, Model model,
			Authentication authentication, HttpSession session) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

		if (nodeId == null) {
			nodeId = principal.getInstId();
		} else {
			nodeId = XorMaskHelper.unmask(nodeId);
		}

		List<Object[]> eventList = service.findAllEventsApplicable(nodeId);
		NodeDTO node = nodeService.findById(nodeId);
		Node.Type userNodeType = principal.getNodeType();
		model.addAttribute("parentId", XorMaskHelper.mask(nodeId));

		model.addAttribute("nodeType", userNodeType.name());
		model.addAttribute("nodeName", node.getNodeName());
		model.addAttribute("eventList", eventList);

		return "fragments/events/allevents";
	}

	@GetMapping("/details/{id}")
	public String viewEventById(@PathVariable Long id, Model model) {
		EventDTO event = service.findById(XorMaskHelper.unmask(id));
		model.addAttribute("event", event);
		model.addAttribute("pageTitle", "Event Details: " + event.getEventName());
		return "fragments/events/view";
	}

	@GetMapping("/add/{id}")
	public String showAddEventForm(@PathVariable(value = "id") Long mNodeId, Model model) {
		Long nodeId = XorMaskHelper.unmask(mNodeId);

		model.addAttribute("event", new EventDTO());
		model.addAttribute("pageTitle", "Add Event");

		if (nodeId != null) {
			NodeDTO node = nodeService.findById(nodeId);
			model.addAttribute("hostNode", node);
			model.addAttribute("hostNodeId", mNodeId); // Pass masked ID
		}

		List<EventItemDTO> allItems = eventItemService.findAll();
		model.addAttribute("eventItems", allItems);

		return "fragments/events/add";
	}

	@PostMapping("/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addEvent(@PathVariable(value = "id") Long mNodeId,
			@Valid @ModelAttribute EventDTO eventDTO, BindingResult result,
			@RequestParam(value = "seniorItemIds", required = false) List<Long> seniorItemIds,
			@RequestParam(value = "juniorItemIds", required = false) List<Long> juniorItemIds,
			@RequestParam(value = "subjuniorItemIds", required = false) List<Long> subjuniorItemIds,
			Authentication authentication, HttpServletRequest request) {

		Long nodeId = XorMaskHelper.unmask(mNodeId);

		// Validate that at least one item is selected
		if ((seniorItemIds == null || seniorItemIds.isEmpty()) && (juniorItemIds == null || juniorItemIds.isEmpty())
				&& (subjuniorItemIds == null || subjuniorItemIds.isEmpty())

		) {
			result.rejectValue("eventItems", "error.event", "Please select at least one item from catogories");
		}

		// Set host information
		NodeDTO node = nodeService.findById(nodeId);
		if (node != null) {
			eventDTO.setEventHost(node.getNodeType());
			eventDTO.setEventHostId(nodeId);
		}

		// Validate allowed node IDs (security check similar to officials)
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		List<Long> allowedNodeIds = nodeService.findAllowedNodeIds(principal.getInstId());
		if (!allowedNodeIds.contains(nodeId)) {
			throw new SecurityException("Invalid node submitted!");
		}

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "champ_eventbynode/" + mNodeId);
		additionalData.put("target", "events_target");

		return handleRequest(result, () -> {
			// Save the event first
			EventDTO savedEvent = service.save(eventDTO);

			// Save item mappings if provided
			if (seniorItemIds != null && !seniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(savedEvent.getEventId(), seniorItemIds, EventItemMap.Category.SENIOR);
			}
			if (juniorItemIds != null && !juniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(savedEvent.getEventId(), juniorItemIds, EventItemMap.Category.JUNIOR);
			}
			if (subjuniorItemIds != null && !subjuniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(savedEvent.getEventId(), subjuniorItemIds,
						EventItemMap.Category.SUBJUNIOR);
			}

			return savedEvent;
		}, "Event added successfully", additionalData);
	}

	@GetMapping("/edit/{id}")
	public String editEventForm(@PathVariable Long id, Model model, Authentication authentication) {
		Long eventId = XorMaskHelper.unmask(id);

		// Get the event with security check
		EventDTO event = service.findById(eventId);

		// Security validation - ensure user has access to this event's node
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		List<Long> allowedNodeIds = nodeService.findAllowedNodeIds(principal.getInstId());
		if (!allowedNodeIds.contains(event.getEventHostId())) {
			throw new SecurityException("Access denied to this event!");
		}

		model.addAttribute("event", event);
		model.addAttribute("pageTitle", "Edit Event");
		model.addAttribute("eventId", id); // Pass masked ID

		// Get all available items
		List<EventItemDTO> allItems = eventItemService.findAll();
		model.addAttribute("eventItems", allItems);

		// Get currently selected items
		Map<EventItemMap.Category, List<Long>> selectedItems = eventItemMapService
				.findItemsByEventIdGroupedByCategory(eventId);
		model.addAttribute("seniorItemIds",
				selectedItems.getOrDefault(EventItemMap.Category.SENIOR, Collections.emptyList()));
		model.addAttribute("juniorItemIds",
				selectedItems.getOrDefault(EventItemMap.Category.JUNIOR, Collections.emptyList()));
		model.addAttribute("subjuniorItemIds",
				selectedItems.getOrDefault(EventItemMap.Category.SUBJUNIOR, Collections.emptyList()));

		return "fragments/events/edit";
	}

	@PostMapping("/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable("id") Long mEventId,
			@Valid @ModelAttribute EventDTO eventDTO, BindingResult result,
			@RequestParam(value = "seniorItemIds", required = false) List<Long> seniorItemIds,
			@RequestParam(value = "juniorItemIds", required = false) List<Long> juniorItemIds,
			@RequestParam(value = "subjuniorItemIds", required = false) List<Long> subjuniorItemIds,
			Authentication authentication) {

		Long eventId = XorMaskHelper.unmask(mEventId);

		// Validate that at least one item is selected
		if ((seniorItemIds == null || seniorItemIds.isEmpty()) && (juniorItemIds == null || juniorItemIds.isEmpty())
				&& (subjuniorItemIds == null || subjuniorItemIds.isEmpty())

		) {
			result.rejectValue("eventItems", "error.event", "Please select at least one item from catogories");
		}

		// Security validation
		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		EventDTO existingEvent = service.findById(eventId);
		List<Long> allowedNodeIds = nodeService.findAllowedNodeIds(principal.getInstId());
		if (!allowedNodeIds.contains(existingEvent.getEventHostId())) {
			throw new SecurityException("Access denied to this event!");
		}

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "champ_eventbynode/" + XorMaskHelper.mask(existingEvent.getEventHostId()));
		additionalData.put("target", "events_target");

		return handleRequest(result, () -> {
			// Update the event first
			EventDTO updatedEvent = service.update(eventId, eventDTO);

			// First delete all existing mappings for this event
			eventItemMapService.deleteByEventId(eventId);

			// Then save the new mappings
			if (seniorItemIds != null && !seniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(eventId, seniorItemIds, EventItemMap.Category.SENIOR);
			}
			if (juniorItemIds != null && !juniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(eventId, juniorItemIds, EventItemMap.Category.JUNIOR);
			}
			if (subjuniorItemIds != null && !subjuniorItemIds.isEmpty()) {
				eventItemMapService.saveMappings(eventId, subjuniorItemIds, EventItemMap.Category.SUBJUNIOR);
			}

			return updatedEvent;
		}, "Event updated successfully", additionalData);
	}

	@GetMapping("/participation/{eventid}/{nodeid}")
	public String participationForm(@PathVariable("eventid") Long mEventId,
	                                @PathVariable("nodeid") Long mNodeId,
	                                Model model) {

	    Long eventId = XorMaskHelper.unmask(mEventId);
	    Long nodeId = XorMaskHelper.unmask(mNodeId);

	    // Safe fetch of members
	    Map<String, Map<String, List<CoreUser>>> memberMatrix =
	            Objects.requireNonNullElse(memberUserService.getMembersMatrix(nodeId), Collections.emptyMap());

	    // Fetch all EventItemMap for the event safely
	    Map<EventItemMap.Category, List<EventItemMap>> eventItemsByCategory =
	            Objects.requireNonNullElse(eventItemMapService.getEventItemMatrix(eventId), Collections.emptyMap());

	    // Flatten list safely
	    List<EventItemMap> allItems = eventItemsByCategory.values().stream()
	            .flatMap(list -> list.stream().filter(Objects::nonNull))
	            .distinct()
	            .collect(Collectors.toList());

	    // Unique EventItems
	    List<EventItem> uniqueEventItems = allItems.stream()
	            .map(EventItemMap::getItem)
	            .filter(Objects::nonNull)
	            .distinct()
	            .collect(Collectors.toList());

	    // Category as string map for template
	    Map<String, List<EventItemMap>> eventItemsByCategoryStrKey = new HashMap<>();
	    eventItemsByCategory.forEach((cat, list) -> eventItemsByCategoryStrKey.put(cat.name(), list));

	    // Item -> category -> list map
	    Map<Long, Map<String, List<EventItemMap>>> itemCategoryMap = new HashMap<>();
	    for (EventItem uniqueItem : uniqueEventItems) {
	        Map<String, List<EventItemMap>> catMap = new HashMap<>();
	        for (Map.Entry<EventItemMap.Category, List<EventItemMap>> entry : eventItemsByCategory.entrySet()) {
	            List<EventItemMap> filtered = entry.getValue().stream()
	                    .filter(Objects::nonNull)
	                    .filter(eim -> eim.getItem() != null && eim.getItem().getEvitemId().equals(uniqueItem.getEvitemId()))
	                    .toList();
	            if (!filtered.isEmpty()) catMap.put(entry.getKey().name(), filtered);
	        }
	        itemCategoryMap.put(uniqueItem.getEvitemId(), catMap);
	    }
	    
	    Set<String> selectedKeys  = memberEventItemService.getSelectedKeys(eventId,nodeId);
	    model.addAttribute("selectedKeys", selectedKeys);

	    // Add attributes safely
	    model.addAttribute("itemCategoryMap", itemCategoryMap);
	    model.addAttribute("eventItemsByCategoryStrKey", eventItemsByCategoryStrKey);
	    model.addAttribute("memberMatrix", memberMatrix);
	    model.addAttribute("uniqueEventItems", uniqueEventItems);
	    model.addAttribute("allEventItems", allItems);
	    model.addAttribute("eventItemsByCategory", eventItemsByCategory);    
	    model.addAttribute("eventId", mEventId);
	    model.addAttribute("nodeId", mNodeId);
	    model.addAttribute("pageTitle", "Add Event Participants");
	    
	    
        Map<String, Map<String, Map<String, List<MemberEventItem>>>> matrix = memberEventItemService.getParticipationMatrix(eventId);
        model.addAttribute("matrix", matrix);
        
        

	    return "fragments/events/participation";
	}

	
	
	@PostMapping("/participation/{eventId}/{nodeId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addParticipation(
	        @PathVariable("eventId") Long mEventId,
	        @PathVariable("nodeId") Long mNodeId,
	        @RequestParam Map<String, String> requestParams,
	        @RequestParam(name = "pageParams", required = false) String pageParams) {
		
		
		Long eventId = XorMaskHelper.unmask(mEventId);
		Long nodeId = XorMaskHelper.unmask(mNodeId);

	    // Page reload target
	    Map<String, Object> extra = new HashMap<>();
	    extra.put("loadnext", "participation/" + eventId + "/" + nodeId +
	            (pageParams != null ? "?" + pageParams : ""));

	    memberEventItemService.saveParticipation(eventId, nodeId, requestParams);
	    Map<String,Object> body = new HashMap<>();
	    body.put("message", "Participation saved successfully");
	    body.put("status", "success");
	    body.put("loadnext", "champ_participation/" + mEventId + "/" + mNodeId +
	            (pageParams != null ? "?" + pageParams : ""));
	    body.put("target", "events_target");
	    return ResponseEntity.ok(body);
	}


	@GetMapping("/html/listparticipants")
	public String listParticipants(@RequestParam(name = "eventId", required = false) Long eventId, Model model) {

		List<MemberEventDTO> memberEeventDTO = memberEventService.findByEvent(eventId);

		List<EventItemDTO> allItems = eventItemService.findAll(); // or eventService.getItemsByEventId(eventId)

		Map<Long, String> itemIdToName = allItems.stream()
				.collect(Collectors.toMap(EventItemDTO::getEvitemId, EventItemDTO::getEvitemName));

		// logInfo("eventItems: {}",itemIdToName);

		model.addAttribute("itemNameMap", itemIdToName);
		model.addAttribute("partList", memberEeventDTO);
		model.addAttribute("pageTitle", "List of Participants");
		model.addAttribute("event", new EventDTO());

		return "fragments/events/list_participants";
	}

	@GetMapping("/html/listitems")
	public String listItems(@RequestParam(name = "eventId") Long eventId, Model model) {

		List<EventItemDTO> juniorItems = service.getEventItemsByCategory(eventId, EventItemMap.Category.JUNIOR);
		List<EventItemDTO> seniorItems = service.getEventItemsByCategory(eventId, EventItemMap.Category.SENIOR);

		logInfo("juniorItems: {}", juniorItems);
		logInfo("seniorItems: {}", seniorItems);

		model.addAttribute("juniorItems", juniorItems);
		model.addAttribute("seniorItems", seniorItems);
		model.addAttribute("eventId", eventId);
		model.addAttribute("pageTitle", "Event Items");

		return "fragments/events/list_items";
	}

	@GetMapping("/html/selectitems")
	public String selectItems(@RequestParam("eventId") Long eventId,
			@RequestParam("selectedItemId") Long selectedItemId, HttpServletRequest request, Model model) {

		// Fetch filtered event member list
		List<Object[]> resultList = service.getMemberEventsWithFilters(selectedItemId, eventId, null, null, null, null,
				null);

		List<EventItemDTO> allItems = eventItemService.findAll(); // Or fetch only for this event if optimized
		Map<Long, String> itemIdToName = allItems.stream()
				.collect(Collectors.toMap(EventItemDTO::getEvitemId, EventItemDTO::getEvitemName));

		// Populate model attributes for rendering
		model.addAttribute("resultList", resultList);
		model.addAttribute("eventId", eventId);
		model.addAttribute("selectedItemId", selectedItemId);
		model.addAttribute("itemNameMap", itemIdToName);

		return "fragments/events/list_items_members";
	}

	@PostMapping("/html/save_scores")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveScores(@RequestParam("eventId") Long eventId,
			@RequestParam("itemId") Integer selectedItemId, @RequestParam Map<String, String> allParams,
			HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Long resultEntryBy = session != null ? (Long) session.getAttribute("USER_ID") : null;

		Map<String, Object> additionalData = new HashMap<>();

		try {
			// Loop through all "scores[memberId]" entries
			for (Map.Entry<String, String> entry : allParams.entrySet()) {
				String key = entry.getKey();

				if (key.startsWith("scores[")) {
					try {
						// Extract the memberEventId from key "scores[60]"
						String memberEventIdStr = key.substring("scores[".length(), key.length() - 1);
						Long memberEventId = Long.parseLong(memberEventIdStr);
						String scoreValue = entry.getValue(); // e.g., "A"

						// Fetch MemberEventDTO by memberEventId (more efficient than eventId+memberId)
						MemberEventDTO memberEvent = (MemberEventDTO) memberEventService.findByEventAndMember(eventId,
								memberEventId);
						if (memberEvent != null) {
							// Set or update the score for selectedItemId
							Map<Integer, String> items = memberEvent.getItems();
							if (items == null) {
								items = new HashMap<>();
								memberEvent.setItems(items);
							}
							// Changed to String key to match your previous implementation
							items.put(selectedItemId, scoreValue);

							memberEvent.setResultDate(LocalDateTime.now());
							memberEvent.setResultEntryBy(resultEntryBy);

							// Save updated data
							memberEventService.save(memberEvent);
						}
					} catch (NumberFormatException e) {
						// Handle invalid memberEventId format
						return ResponseEntity.badRequest()
								.body(Map.of("error", "Invalid member ID format in parameter: " + key));
					}
				}
			}

			return ResponseEntity.ok().body(Map.of("status", "success", "message", "Scores updated successfully",
					"additionalData", additionalData));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("error", "Error saving scores: " + e.getMessage()));
		}
	}

	@PostMapping("/html/cancel/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> cancelEvent(@PathVariable Long id) {
		service.softDeleteById(id);
		return buildResponse("Event cancelled successfully", Map.of("redirect", "/events/html"));
	}

	@GetMapping("/html/active")
	public String getActiveEvents(Model model) {
		List<EventDTO> activeEvents = service.findActiveEventsOnDate(LocalDate.now());
		model.addAttribute("events", activeEvents);
		model.addAttribute("pageTitle", "Active Events");
		return "fragments/active_events";
	}
}