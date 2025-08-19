package com.dms.kalari.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Validator; // For the validator object
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.DocumentsDTO;
import com.dms.kalari.dto.PropertyPayOptionDTO;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisPropertyPayOption;
import com.dms.kalari.repository.MisDocumentsRepository;
import com.dms.kalari.service.MisDocumentDataService;
import com.dms.kalari.service.MisPropertyPayOptionService;

@Controller
@RequestMapping("/propertyPayOptions")
public class MisPropertyPayOptionHtmlController
		extends BaseController<PropertyPayOptionDTO, MisPropertyPayOptionService> {

	private final Validator validator;
	
	@Autowired
    private MisDocumentDataService misDocumentDataService;

	@Autowired
	public MisPropertyPayOptionHtmlController(MisPropertyPayOptionService service, Validator validator) {
		super(service);
		this.validator = validator; // Injecting the validator through the constructor
	}

	@GetMapping("/html")
	public String listPayOptions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "poptId") String sortField,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String search,
			Model model) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}", page, size,
				sortField, sortDir, search);

		Page<PropertyPayOptionDTO> payOptionPage = service.findAllPaginate(pageable, search);

		logInfo("Pay Option Page - Total Elements: {}, Total Pages: {}", payOptionPage.getTotalElements(),
				payOptionPage.getTotalPages());
		logInfo("Pay Options: {}", payOptionPage.getContent());

		setupPagination(model, payOptionPage, sortField, sortDir);

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "Property Payment Options ");
		model.addAttribute("pageUrl", "/propertyPayOptions/html");

		return "fragments/property_pay_option_list";
	}

	@GetMapping("/html/{id}")
	public String viewPayOptionById(@PathVariable Long id, Model model) {
		model.addAttribute("payOption", service.findById(id));
		model.addAttribute("pageTitle", "Pay Option Detail ");
		return "fragments/property_pay_option_detail";
	}

	@GetMapping("/html/add/{refId}")
	public String showAddPayOptionForm(@PathVariable("refId") Long refId, Model model) {
		model.addAttribute("pageTitle", "Add Property Payment Option ");
		model.addAttribute("payOption", new PropertyPayOptionDTO());
		model.addAttribute("refId", refId);
		
		
		
	    List<PropertyPayOptionDTO> payOptions = service.findByPoptDocId(refId);
	    model.addAttribute("payOptions", payOptions);


	    // Add the existing payment options to the model
	    model.addAttribute("payOptions", payOptions);

		return "fragments/payment_schedule";
	}

	@PostMapping("/html/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addPayOption(@RequestParam("popt_date[]") List<LocalDate> poptDates,
	        @RequestParam("popt_amount[]") List<BigDecimal> poptAmounts,
	        @RequestParam("popt_type[]") List<Short> poptTypes,
	        @RequestParam(value = "popt_bank[]", required = false) List<Short> poptBanks,
	        @RequestParam("poptDocId") Long poptDocId) {

	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("loadnext", "view/documents/html");

	    List<PropertyPayOptionDTO> payOptionDTOs = new ArrayList<>();
	    Map<String, String> errors = new HashMap<>(); // Error map for storing validation errors
	    
	    DocumentsDTO existingDocument = misDocumentDataService.findById(poptDocId);


	    for (int i = 0; i < poptTypes.size(); i++) {
	        PropertyPayOptionDTO dto = new PropertyPayOptionDTO();

	        // Retrieve values based on the current index i, with boundary checks
	        dto.setPoptDate(i < poptDates.size() ? poptDates.get(i) : null);
	        dto.setPoptAmount(i < poptAmounts.size() ? poptAmounts.get(i) : null);
	        dto.setPoptType(poptTypes.get(i));
	        dto.setPoptDocId(poptDocId);
	        dto.setPoptPropId(existingDocument.getDocRefId());
	        
	        dto.setPoptBank(poptBanks != null && i < poptBanks.size() ? poptBanks.get(i) : null);
	        

	        
	        
		    logInfo("Pay Options: {}", poptTypes.size());

	        // Custom validation for poptBank based on poptType
	        Short poptType = dto.getPoptType(); // Get payment type for the current index
	        Short poptBank = dto.getPoptBank(); // Get bank for the current index
	        
	        if (poptType != null && poptType != 1 && poptBank == null) {
	            errors.put("poptBank[" + i + "]", "Bank is required when type is not Cash");
	        }

	        // Validate each DTO separately using standard validation annotations
	        try {
	            validatePayOptionDTO(dto); // Existing validation logic
	            payOptionDTOs.add(dto); // Add DTO if validation succeeds
	        } catch (ConstraintViolationException e) {
	            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
	                // Add errors in the format that matches the input names: e.g., popt_date[0],
	                // popt_amount[1]
	                String field = violation.getPropertyPath().toString();
	                if (field.contains(".")) {
	                    field = field.substring(field.lastIndexOf('.') + 1); // Extracts the last part after the dot
	                }
	                errors.put(field + "[" + i + "]", violation.getMessage());
	            }
	        }
	    }

	    // If there are errors, return them in the appropriate format
	    if (!errors.isEmpty()) {
	        return buildErrorResponse(errors, "Validation error occurred.");
	    }

	    try {
	        for (PropertyPayOptionDTO payOptionDTO : payOptionDTOs) {
	            service.save(payOptionDTO);
	        }
	        return buildResponse("Payment Schedule added successfully", additionalData);
	    } catch (Exception e) {
	        return buildErrorResponse(new HashMap<>(), "Error occurred: " + e.getMessage());
	    }
	}


	private void validatePayOptionDTO(PropertyPayOptionDTO dto) throws ConstraintViolationException {
		Set<ConstraintViolation<PropertyPayOptionDTO>> violations = validator.validate(dto);

		if (!violations.isEmpty()) {
			// Convert violations into a more user-friendly error map
			Map<String, String> errors = violations.stream()
					.collect(Collectors.toMap(v -> "popt_" + v.getPropertyPath(), ConstraintViolation::getMessage));

			// Throwing a ConstraintViolationException with a more user-friendly message
			throw new ConstraintViolationException(new HashSet<>(violations));
		}
	}

	@GetMapping("/html/edit/{id}")
	public String editPayOption(@PathVariable Long id, Model model) {
		model.addAttribute("payOption", service.findById(id));
		return "fragments/edit_property_pay_option";
	}

	@PostMapping("/html/update/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updatePayOption(@PathVariable("id") Long id,
			@Valid @ModelAttribute PropertyPayOptionDTO payOptionDTO, BindingResult result) {
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "/propertyPayOptions/html");
		return handleRequest(result, () -> service.update(id, payOptionDTO),
				"Property Payment Option updated successfully", additionalData);
	}
}
