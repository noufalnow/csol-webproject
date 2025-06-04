package com.example.tenant_service.controller_html;

import com.example.tenant_service.common.BaseController;
import com.example.tenant_service.dto.EventItemDTO;
import com.example.tenant_service.service.EventItemService;
import com.example.tenant_service.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/event-items")
public class EventItemHtmlController extends BaseController<EventItemDTO, EventItemService> {

    @Autowired
    public EventItemHtmlController(EventItemService service) {
        super(service);
    }

    /**
     * Paginated list of Event Items
     */
    @GetMapping("/html")
    public String listEventItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "evitemId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.fromString(sortDir), sortField)
        );

        Page<EventItemDTO> itemPage = service.findAllPaginate(pageable, search);

        // Set up pagination attributes in the model
        setupPagination(model, itemPage, sortField, sortDir);

        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Event Item List");
        model.addAttribute("pageUrl", "/event-items/html");

        return "fragments/manage/items/list";
    }

    /**
     * View details of a single Event Item
     */
    @GetMapping("/html/{id}")
    public String viewEventItem(@PathVariable Long id, Model model) {
        EventItemDTO dto = service.findById(id);
        model.addAttribute("eventItem", dto);
        model.addAttribute("pageTitle", "Event Item Details");
        return "fragments/manage/items/detail";
    }

    /**
     * Show add form
     */
    @GetMapping("/html/add")
    public String showAddForm(Model model) {
        model.addAttribute("pageTitle", "Add New Event Item");
        model.addAttribute("eventItem", new EventItemDTO());
        return "fragments/manage/items/add";
    }

    /**
     * Handle add form submission
     */
    @PostMapping("/html/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addEventItem(
            @Valid @ModelAttribute("eventItem") EventItemDTO eventItemDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/event-items/html");

        return handleRequest(
            result,
            () -> service.save(eventItemDTO),
            "Event Item added successfully",
            additionalData
        );
    }

    /**
     * Show edit form
     */
    @GetMapping("/html/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EventItemDTO dto = service.findById(id);
        model.addAttribute("eventItem", dto);
        model.addAttribute("pageTitle", "Edit Event Item");
        return "fragments/manage/items/edit";
    }

    /**
     * Handle edit form submission
     */
    @PostMapping("/html/update/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateEventItem(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("eventItem") EventItemDTO eventItemDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/event-items/html");

        return handleRequest(
            result,
            () -> service.update(id, eventItemDTO),
            "Event Item updated successfully",
            additionalData
        );
    }
}
