package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.EventService;
import com.example.tenant_service.service.MemberEventService;
import com.example.tenant_service.service.NodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.EventDTO;
import com.example.tenant_service.dto.MemberEventDTO;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.entity.Event;
import com.example.tenant_service.entity.Node;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/events")
public class EventHtmlController extends BaseController<EventDTO, EventService> {

    private final NodeService nodeService;
    private final MemberEventService memberEventService;
    

    public EventHtmlController(EventService eventService, NodeService nodeService, MemberEventService memberEventService) {
        super(eventService);
        this.nodeService = nodeService;
        this.memberEventService = memberEventService;
    }

    @GetMapping("/html")
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventPeriodStart") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

        logInfo("Fetching events - Page: {}, Size: {}, Sort: {}, Search: {}", 
                page, size, sortField + " " + sortDir, search);

        Page<EventDTO> eventPage = service.findAllPaginate(pageable, search);

        setupPagination(model, eventPage, sortField, sortDir);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Event List");
        model.addAttribute("pageUrl", "/events/html");
        model.addAttribute("today", LocalDate.now());

        return "fragments/event_list";
    }

    //@GetMapping("/html/byhost")
    //public String listEventsByHost(Model model, HttpServletRequest request) {
    
	@GetMapping({ "/html/byhost", "/html/byhost/{id}" }) // Supports both patterns
	public String listEventsByHost(@PathVariable(value = "id", required = false) Long parentId, HttpSession session,
			Model model) {
    	

		if (parentId != null) {
			session.setAttribute("ParentId", parentId); // for data entry
		} else {
			parentId = (Long) session.getAttribute("NODE_ID");
		}
	    
		NodeDTO node = nodeService.findById(parentId);

		Node.Type nodeType = (Node.Type) node.getNodeType();
		Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");
		
		
		logInfo("Request Parameters - Node Type: {}, User Node Type: {},Selected Node: {}", nodeType.getLevel(), userNodeType.getLevel(),parentId);

		if (nodeType.getLevel() >= userNodeType.getLevel()) {
			model.addAttribute("allowAddEvent", true);
		} else if (userNodeType == nodeType && (node.getNodeId() == session.getAttribute("NODE_ID"))) {
			model.addAttribute("allowAddEvent", true);
		}
		
	    
	    
	    

        List<EventDTO> events = service.findByHostNode(parentId);
        model.addAttribute("events", events);
        model.addAttribute("target", "events_target");
        return "fragments/events/node_events";
    }

    @GetMapping("/html/{id}")
    public String viewEventById(@PathVariable Long id, Model model) {
        EventDTO event = service.findById(id);
        model.addAttribute("event", event);
        model.addAttribute("pageTitle", "Event Details: " + event.getEventName());
        return "fragments/event_detail";
    }

    @GetMapping("/html/addevent")
    public String showAddEventForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long ParentId = session != null ? (Long) session.getAttribute("ParentId") : null;

        model.addAttribute("pageTitle", "Add Event");
        model.addAttribute("event", new EventDTO());
        
        if (ParentId != null) {
            model.addAttribute("hostNode", nodeService.findById(ParentId));
            model.addAttribute("hostNodeId", ParentId);
        }

        return "fragments/events/add_event";
    }

    @PostMapping("/html/addevent")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addEvent(
            @Valid @ModelAttribute EventDTO eventDTO,
            BindingResult result,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long parentId = session != null ? (Long) session.getAttribute("ParentId") : null;
        
        if (parentId != null) {
            NodeDTO node = nodeService.findById(parentId);
            if (node != null) {
                eventDTO.setEventHost(node.getNodeType());  // Direct assignment
                eventDTO.setEventHostId(parentId);
            }
        }

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/events/html/byhost");
        
        
        additionalData.put("target", "users_target");

        return handleRequest(result, 
                () -> service.save(eventDTO), 
                "Event added successfully", 
                additionalData);
    }

    @GetMapping("/html/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        EventDTO event = service.findById(id);
        model.addAttribute("event", event);
        //model.addAttribute("hostTypes", Event.HostType.values());
        List<NodeDTO> nodes = nodeService.findAll();
        model.addAttribute("nodes", nodes);
        return "fragments/edit_event";
    }

    @PostMapping("/html/update/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute EventDTO eventDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/events/html");

        return handleRequest(result, 
                () -> service.update(id, eventDTO), 
                "Event updated successfully", 
                additionalData);
    }
    
    
    @GetMapping("/html/listparticipants")
    public String listParticipants(@RequestParam(name = "eventId", required = false) Long eventId,Model model) {


        List<MemberEventDTO> memberEeventDTO =memberEventService.findByEvent(eventId);
    	
        model.addAttribute("partList", memberEeventDTO);
        model.addAttribute("pageTitle", "List of Participants");
        model.addAttribute("event", new EventDTO());
        

        return "fragments/events/list_participants";
    }
    
    @GetMapping("/html/listitems")
    public String listItems(@RequestParam(name = "eventId", required = false) Long eventId,Model model) {


        List<MemberEventDTO> memberEeventDTO =memberEventService.findByEvent(eventId);
    	
        model.addAttribute("partList", memberEeventDTO);
        model.addAttribute("pageTitle", "Event Items");
        model.addAttribute("eventId", eventId);
        

        return "fragments/events/list_items";
    }
    
    

    @GetMapping("/html/selectitems")
    public String selectItems(
            @RequestParam("eventId") Long eventId,
            @RequestParam("selectedItemId") Long selectedItemId,
            HttpServletRequest request,
            Model model) {

        // Fetch filtered event member list
        List<Object[]> resultList = service.getMemberEventsWithFilters(
        		selectedItemId, eventId, null, null, null, null,null);

        // Populate model attributes for rendering
        model.addAttribute("resultList", resultList);
        model.addAttribute("eventId", eventId);
        model.addAttribute("selectedItemId", selectedItemId);

        return "fragments/events/list_items_members";
    }
    
    
    @PostMapping("/html/save_scores")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveScores(
            @RequestParam("eventId") Long eventId,
            @RequestParam("itemId") Integer selectedItemId,
            @RequestParam Map<String, String> allParams,
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
                        MemberEventDTO memberEvent = (MemberEventDTO) memberEventService.findByEventAndMember(eventId, memberEventId);
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

            return ResponseEntity.ok()
                    .body(Map.of("status", "success", 
                                "message", "Scores updated successfully",
                                "additionalData", additionalData));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error saving scores: " + e.getMessage()));
        }
    }



    

    @PostMapping("/html/cancel/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelEvent(@PathVariable Long id) {
        service.softDeleteById(id);
        return buildResponse("Event cancelled successfully", 
                Map.of("redirect", "/events/html"));
    }

    @GetMapping("/html/active")
    public String getActiveEvents(Model model) {
        List<EventDTO> activeEvents = service.findActiveEventsOnDate(LocalDate.now());
        model.addAttribute("events", activeEvents);
        model.addAttribute("pageTitle", "Active Events");
        return "fragments/active_events";
    }
}