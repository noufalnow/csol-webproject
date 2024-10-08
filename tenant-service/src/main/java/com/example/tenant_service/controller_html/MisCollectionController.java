package com.example.tenant_service.controller_html;

import com.example.tenant_service.dto.CollectionDetDTO;
import com.example.tenant_service.dto.CustomerPaymentDTO;
import com.example.tenant_service.dto.DocumentsViewDTO;
import com.example.tenant_service.dto.MisCollectionDTO;
import com.example.tenant_service.dto.PropertyPayOptionDTO;
import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.entity.MisCollection;
import com.example.tenant_service.entity.MisCollectionDet;
import com.example.tenant_service.entity.MisPropertyPayOption;
import com.example.tenant_service.mapper.MisCollectionMapper;
import com.example.tenant_service.service.MisCollectionDetService;
import com.example.tenant_service.service.MisCollectionService;
import com.example.tenant_service.service.MisDocumentViewService;
import com.example.tenant_service.service.MisPropertyPayOptionService;
import com.example.tenant_service.service.MisTenantsService;

import groovy.lang.Tuple;
import jakarta.validation.Valid;

import com.example.tenant_service.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.tenant_service.entity.MisCollection;
import com.example.tenant_service.entity.MisCollectionDet;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collections")
public class MisCollectionController extends BaseController<MisCollectionDTO, MisCollectionService> {

	private final MisTenantsService tenantsService;
	private final MisDocumentViewService documentViewService;
	private final MisPropertyPayOptionService payOptionService;

	private final MisCollectionDetService misCollectionDetService;
	private final MisPropertyPayOptionService misPropertyPayOptionService;
	private final MisCollectionMapper misCollectionMapper;

	@Autowired
	public MisCollectionController(MisCollectionService misCollectionService, MisTenantsService tenantsService,
			MisDocumentViewService documentViewService, MisPropertyPayOptionService payOptionService,
			MisCollectionDetService misCollectionDetService, MisPropertyPayOptionService misPropertyPayOptionService,
			MisCollectionMapper misCollectionMapper

	) {
		super(misCollectionService);
		this.tenantsService = tenantsService;
		this.documentViewService = documentViewService;
		this.payOptionService = payOptionService;

		this.misCollectionDetService = misCollectionDetService;
		this.misPropertyPayOptionService = misPropertyPayOptionService;
		this.misCollectionMapper = misCollectionMapper;

	}

	@GetMapping("/html")
	public String listCollections(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "collId") String sortField,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String search,
			Model model) {

		if (search != null) {
			search = URLDecoder.decode(search, StandardCharsets.UTF_8);
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
		Page<MisCollectionDTO> collectionPage = service.findAllPaginate(pageable, search);

		// Set up the sorting status map
		Map<String, String> sortStatus = new HashMap<>();
		sortStatus.put(sortField, sortDir);

		setupPagination(model, collectionPage, sortField, sortDir);
		model.addAttribute("sortStatus", sortStatus);
		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "Collections List");
		model.addAttribute("pageUrl", "/collections/html");

		return "fragments/collection_list";
	}

	// GET request to display the 'Add Collection' form
	@GetMapping("/html/add")
	public String showAddCollectionForm(Model model) {
		// Fetch all tenants to list in the form
		List<TenantDTO> tenants = tenantsService.findAll();
		model.addAttribute("tenants", tenants);

		// This is a placeholder for selected documents and payment schedules
		model.addAttribute("selectedDocuments", new ArrayList<>());

		return "fragments/add_collection";
	}

	// AJAX call to fetch documents (agreements) for a selected tenant
	@GetMapping("/documents/{tenantId}")
	@ResponseBody
	public List<DocumentsViewDTO> getDocumentsByTenant(@PathVariable("tenantId") Long docTntId) {

		List<DocumentsViewDTO> documents = documentViewService.findByTenantIdPendingPayments(docTntId);
		documents.forEach(doc -> System.out.println("Document ID: " + doc.getDocId() + ", Doc No.: " + doc.getDocNo()));
		return documents;

		// return documentViewService.findByTenantId(docTntId);
	}

	// AJAX call to fetch payment schedules for a selected agreement
	@GetMapping("/schedules/{documentId}")
	@ResponseBody
	public List<PropertyPayOptionDTO> getPaymentSchedulesByDocument(@PathVariable("documentId") Long documentId) {
		return payOptionService.findByPoptDocIdAndPending(documentId);
	}

	// POST request to handle the collection process and save the selected schedules
	@PostMapping("/html/add")
	public ResponseEntity<Map<String, Object>> addCollection(@Valid @ModelAttribute MisCollectionDTO collectionDTO,
	                                                         BindingResult result) {
	    Map<String, Object> additionalData = new HashMap<>();
	    additionalData.put("loadnext", "/collections/html");

	    // Log the received data
	    logInfo("Received Collection data: {}", collectionDTO);

	    // Validate if any schedules were selected
	    if (collectionDTO.getSelectedSchedules() == null || collectionDTO.getSelectedSchedules().isEmpty()) {
	        logInfo("Validation failed: No schedules selected.");
	        result.reject("selectedSchedules", "You must select at least one schedule.");
	        return handleRequest(result, null, "Validation failed", additionalData);
	    }

	    return handleRequest(result, () -> {
	        // 1. Update PropertyPayOptionDTO (mark as paid)
	        logInfo("Updating pay option status for selected schedules: {}", collectionDTO.getSelectedSchedules());

	        List<PropertyPayOptionDTO> updatedPayOptionDTOs = collectionDTO.getSelectedSchedules().stream().map(scheduleId -> {
	            logInfo("Fetching pay option for scheduleId: {}", scheduleId);
	            PropertyPayOptionDTO payOptionDTO = payOptionService.findById(scheduleId); // Assuming scheduleId corresponds to PoptId
	            
	            logInfo("Updating payOptionDTO: {}", payOptionDTO);
	            payOptionDTO.setPoptStatus((short) 2); // Status 2 means paid
	            payOptionDTO.setPoptStatusDate(LocalDate.now());
	            PropertyPayOptionDTO savedPayOptionDTO = payOptionService.save(payOptionDTO); // Saving DTO
	            
	            logInfo("Pay option updated and saved: {}", savedPayOptionDTO);
	            return savedPayOptionDTO; // Saving DTO
	        }).collect(Collectors.toList());

	        // 2. Insert into MisCollection
	        logInfo("Creating new MisCollection with Tenant ID: {}", collectionDTO.getTenantId());
	        MisCollection newCollection = new MisCollection();
	        newCollection.setCollCust(collectionDTO.getTenantId());
	        newCollection.setCollAmount(collectionDTO.getNetAmount());
	        newCollection.setCollDiscount(collectionDTO.getCollDiscount()); // Ensure correct data type
	        newCollection.setCollPayDate(LocalDate.now());
	        newCollection.setCollType(1L); // Assuming collection type 1 for now

	        // Map MisCollection entity to MisCollectionDTO using the mapper
	        MisCollectionDTO collectionDTOToSave = misCollectionMapper.toDTO(newCollection);
	        logInfo("Mapped MisCollectionDTO to save: {}", collectionDTOToSave);

	        // Save the MisCollectionDTO
	        MisCollectionDTO savedCollection = service.save(collectionDTOToSave);
	        logInfo("Saved new MisCollection: {}", savedCollection);

	        // 3. Insert into MisCollectionDet for each schedule
	        updatedPayOptionDTOs.forEach(payOptionDTO -> {
	            logInfo("Creating CollectionDetDTO for payOptionId: {}", payOptionDTO.getPoptId());
	            CollectionDetDTO collectionDetDTO = new CollectionDetDTO();
	            collectionDetDTO.setCdetCollId(savedCollection.getCollId());
	            collectionDetDTO.setCdetPoptId(payOptionDTO.getPoptId());
	            collectionDetDTO.setCdetAmtToPay(payOptionDTO.getPoptAmount());
	            collectionDetDTO.setCdetAmtPaid(payOptionDTO.getPoptAmount()); // Assuming full payment
	            
	            misCollectionDetService.save(collectionDetDTO);
	            logInfo("Saved CollectionDetDTO: {}", collectionDetDTO);
	        });

	        logInfo("Collection added successfully.");
	        return savedCollection;
	    }, "Collection added successfully", additionalData);
	}
	
	
	@GetMapping("/html/report")
	public String getCollectionReport(@RequestParam(required = false) Long customer,
	                                  @RequestParam(required = false) String property,
	                                  @RequestParam(required = false) String fromDate,
	                                  @RequestParam(required = false) String toDate,
	                                  Model model) {
	    // Decode parameters if necessary

	    if (property != null) {
	        property = URLDecoder.decode(property, StandardCharsets.UTF_8);
	    }

	    // Parse date range if provided
	    LocalDate from = (fromDate != null && !fromDate.isEmpty()) ? LocalDate.parse(fromDate) : null;
	    LocalDate to = (toDate != null && !toDate.isEmpty()) ? LocalDate.parse(toDate) : null;

	    // Fetch all report data without pagination
	    List<MisCollectionDTO> collections = service.findAllCollectionReport(customer, property, from, to);

	    // Calculate total amount
	    BigDecimal totalAmount = collections.stream()
	                                        .map(MisCollectionDTO::getCollAmount)
	                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    
	    //logInfo("Collections: {}", collections);
        //throw new RuntimeException("Terminating after logging collections for debugging purposes.");


	    model.addAttribute("collections", collections);
	    model.addAttribute("totalAmount", totalAmount);
	    model.addAttribute("customer", customer);
	    model.addAttribute("property", property);
	    model.addAttribute("fromDate", fromDate);
	    model.addAttribute("toDate", toDate);
	    model.addAttribute("pageTitle", "Collection Report");

	    return "fragments/collection_report";
	}


	@GetMapping("/html/payments")
	public String getCustomerPayments(@RequestParam Map<String, String> params, Model model) {
	    // Extract parameters from the `params` map
	    String customer = params.get("customer");
	    String fromDate = params.get("fromDate");
	    String toDate = params.get("toDate");
	    String sortField = params.getOrDefault("sortField", "customerName");  // Default sorting field
	    String sortDir = params.getOrDefault("sortDir", "asc");               // Default sorting direction
	    int page = Integer.parseInt(params.getOrDefault("page", "0"));        // Default page is 0

	    // Decode customer parameter if necessary
	    if (customer != null && !customer.isEmpty()) {
	        customer = URLDecoder.decode(customer, StandardCharsets.UTF_8);
	    }

	    // Parse date range if provided
	    LocalDate from = (fromDate != null && !fromDate.isEmpty()) ? LocalDate.parse(fromDate) : null;
	    LocalDate to = (toDate != null && !toDate.isEmpty()) ? LocalDate.parse(toDate) : null;

	    // Fetch filtered and sorted report data
	    List<Object[]> payments = service.getCustomerPayments(customer, from, to, sortField, sortDir);

	    // Add all required attributes to the model
	    model.addAttribute("payments", payments);
	    model.addAttribute("customer", customer);
	    model.addAttribute("fromDate", fromDate);
	    model.addAttribute("toDate", toDate);
	    model.addAttribute("sortField", sortField);
	    model.addAttribute("sortDir", sortDir);
	    model.addAttribute("pageTitle", "Customer Payments Report");

	    return "fragments/payments_report";  // Make sure this is the correct view name
	}
	
	
	@GetMapping("/html/property")
	public String getPropertyReport(@RequestParam Map<String, String> params, Model model) {
	    String sortField = params.getOrDefault("sortField", "propName");  // Default sorting field
	    String sortDir = params.getOrDefault("sortDir", "asc");           // Default sorting direction
	    
	    // Extract filter parameters
	    // Extract individual parameters and decode them
	    String propNo = params.get("prop_no");
	    if (propNo != null) {
	        propNo = URLDecoder.decode(propNo, StandardCharsets.UTF_8);
	    }

	    String propName = params.get("prop_name");
	    if (propName != null) {
	        propName = URLDecoder.decode(propName, StandardCharsets.UTF_8);
	    }

	    String propFileno = params.get("prop_fileno");
	    if (propFileno != null) {
	        propFileno = URLDecoder.decode(propFileno, StandardCharsets.UTF_8);
	    }

	    String propBuilding = params.get("prop_building");
	    if (propBuilding != null) {
	        propBuilding = URLDecoder.decode(propBuilding, StandardCharsets.UTF_8);
	    }

	    String status = params.get("status");
	    if (status != null) {
	        status = URLDecoder.decode(status, StandardCharsets.UTF_8);
	    }


	    // Pass the filters to the service
	    List<jakarta.persistence.Tuple> properties = service.propertyListReport(
	        sortField, sortDir, propNo, propName, propFileno, propBuilding, status
	    );

	    // Add attributes to the model
	    model.addAttribute("sortField", sortField);
	    model.addAttribute("sortDir", sortDir);
	    model.addAttribute("propNo", propNo);
	    model.addAttribute("propName", propName);
	    model.addAttribute("propFileno", propFileno);
	    model.addAttribute("propBuilding", propBuilding);
	    model.addAttribute("status", status);
	    model.addAttribute("properties", properties);
	    model.addAttribute("pageTitle", "Report: Property List");

	    return "fragments/property_report";
	}








}
