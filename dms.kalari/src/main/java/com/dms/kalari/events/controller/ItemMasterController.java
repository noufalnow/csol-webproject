package com.dms.kalari.events.controller;

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

import com.dms.kalari.common.BaseController;
import com.dms.kalari.events.dto.EventItemDTO;
import com.dms.kalari.events.service.EventItemService;
import com.dms.kalari.util.XorMaskHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("events/item")
public class ItemMasterController extends BaseController<EventItemDTO, EventItemService> {

	@Autowired
	public ItemMasterController(EventItemService service) {
		super(service);
	}

	/** Paginated list of Event Items with masked IDs and paramx */
	@GetMapping("/list")
	public String listEventItems(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortField,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String code,
			@RequestParam(required = false) String name, Model model) {

		if (sortField == null || sortField.isEmpty() || "undefined".equals(sortField)) {
			sortField = "evitemId"; // adjust to your PK
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		Page<EventItemDTO> itemPage = service.findAllPaginate(pageable, code, name);

		// Create a masked copy of the content for view links (don't change the original
		// Page meta)
		List<EventItemDTO> maskedItems = itemPage.getContent().stream().map(dto -> {
			if (dto.getEvitemId() != null) {
				dto.setEvitemId(XorMaskHelper.mask(dto.getEvitemId()));
			}
			return dto;
		}).collect(Collectors.toList());

		// Set up pagination attributes (uses the original Page for metadata)
		setupPagination(model, itemPage, sortField, sortDir);

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("items", maskedItems);
		model.addAttribute("totalCount", itemPage.getTotalElements());

		Map<String,String> paramx = new HashMap<>();
	    paramx.put("code", code  != null ? code.trim() : "");
	    paramx.put("name", name  != null ? name.trim() : "");
	    model.addAttribute("paramx", paramx);

		model.addAttribute("pageTitle", "Event Item List");
		// keep your existing pageUrl used in templates
		model.addAttribute("pageUrl", "/items");

		return "fragments/manage/items/list";
	}

	/** Show add form */
	@GetMapping("/add")
	public String addItem(Model model) {
		model.addAttribute("pageTitle", "Add New Event Item");
		model.addAttribute("eventItem", new EventItemDTO());
		return "fragments/manage/items/add";
	}

	/** Save new item */
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addItem(@Valid @ModelAttribute("eventItem") EventItemDTO eventItemDTO,
			BindingResult result, @RequestParam(name = "pageParams", required = false) String pageParams) {

		Map<String, Object> extra = new HashMap<>();
		extra.put("loadnext",
				"/items" + (pageParams != null && !pageParams.isEmpty() ? "?" + pageParams : ""));

		return handleRequest(result, () -> service.save(eventItemDTO), "Event Item added successfully", extra);
	}

	/** View details (kept original pattern: /add/{id} -> now masked) */
	@GetMapping("/add/{maskedId}")
	public String viewItem(@PathVariable Long maskedId, Model model) {
		Long realId = XorMaskHelper.unmask(maskedId);
		EventItemDTO dto = service.findById(realId);
		// restore masked id in DTO for use in templates/links
		dto.setEvitemId(maskedId);
		model.addAttribute("eventItem", dto);
		model.addAttribute("pageTitle", "Event Item Details");
		return "fragments/manage/items/detail";
	}

	/** Show edit form – unmask before service call */
	@GetMapping("/edit/{maskedId}")
	public String editForm(@PathVariable Long maskedId, Model model) {
		Long realId = XorMaskHelper.unmask(maskedId);
		EventItemDTO dto = service.findById(realId);
		// set masked ID back so the form keeps using masked value
		dto.setEvitemId(maskedId);
		model.addAttribute("eventItem", dto);
		model.addAttribute("pageTitle", "Edit Event Item");
		return "fragments/manage/items/edit";
	}

	/** Update – unmask only in controller */
	@PostMapping("/edit/{maskedId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> editItem(@PathVariable Long maskedId,
			@Valid @ModelAttribute("eventItem") EventItemDTO eventItemDTO, BindingResult result,
			@RequestParam(name = "pageParams", required = false) String pageParams) {

		Long realId = XorMaskHelper.unmask(maskedId);

		Map<String, Object> extra = new HashMap<>();
		extra.put("loadnext",
				"/items" + (pageParams != null && !pageParams.isEmpty() ? "?" + pageParams : ""));

		return handleRequest(result, () -> service.update(realId, eventItemDTO), "Event Item updated successfully",
				extra);
	}
}
