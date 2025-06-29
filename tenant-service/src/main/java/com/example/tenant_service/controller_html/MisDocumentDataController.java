package com.example.tenant_service.controller_html;

import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.service.MisDocumentDataService;
import com.example.tenant_service.service.MisDocumentViewService;
import com.example.tenant_service.service.MisPropertyService;
import com.example.tenant_service.service.MisTenantsService;
import com.example.tenant_service.common.BaseController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/data")
public class MisDocumentDataController extends BaseController<DocumentsDTO, MisDocumentDataService> {

    private final MisDocumentDataService documentDataService;
    private final MisPropertyService propertyService;
    private final MisTenantsService tenantService;
    private final MisDocumentViewService documentViewService;

    @Autowired
    public MisDocumentDataController(
            MisDocumentDataService documentDataService,
            MisDocumentViewService documentViewService,
            MisPropertyService propertyService,
            MisTenantsService tenantService) {
        super(documentDataService);
        this.documentDataService = documentDataService;
        this.documentViewService = documentViewService;
        this.propertyService = propertyService;
        this.tenantService = tenantService;
    }

    @GetMapping("/documents/html/add")
    public String showAddDocumentForm(Model model) {
        model.addAttribute("document", new DocumentsDTO());

        List<PropertyDTO> properties = propertyService.findAll();
        List<TenantDTO> tenants = tenantService.findAll();
        model.addAttribute("properties", properties);
        model.addAttribute("tenants", tenants);

        model.addAttribute("pageTitle", "Add Document ");
        return "fragments/add_document"; // Adjust to your template
    }

    @PostMapping("/documents/html/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addDocument(@Valid @ModelAttribute DocumentsDTO documentDTO,
                                                           BindingResult result) {
    	   	
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "view/documents/html");

        return handleRequest(result, () -> documentDataService.save(documentDTO), "Document added successfully", additionalData);
    }

    @GetMapping("/documents/html/edit/{id}")
    public String editDocument(@PathVariable Long id, Model model) {
        model.addAttribute("document", documentDataService.findById(id));
        
        
	    logInfo("Document Parameters - Document: {}",
	    		documentDataService.findById(id));

        List<PropertyDTO> properties = propertyService.findAll();
        List<TenantDTO> tenants = tenantService.findAll();
        model.addAttribute("properties", properties);
        model.addAttribute("tenants", tenants);

        model.addAttribute("pageTitle", "Edit Document ");
        return "fragments/edit_document"; // Adjust to your template
    }

    @PostMapping("/documents/html/update/{refId}")
    public ResponseEntity<Map<String, Object>> updateDocument(@PathVariable("refId") Long documentId,
                                 @Valid @ModelAttribute DocumentsDTO documentDTO,
                                 BindingResult result, Model model) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "view/documents/html");

        return handleRequest(result, () -> service.update(documentId, documentDTO), 
                             "Documents updated successfully", additionalData);
    }
    
    
    
    @GetMapping("/documents/html/renew/{refId}")
    public String getRenewDocument(@PathVariable Long refId, Model model) {
    	
    	DocumentsDTO document  = documentDataService.findById(refId);
        model.addAttribute("document", document);
        List<DocumentsDTO> versions = documentViewService.findAllByDocAgreement(document.getDocAgreement(),refId);
        model.addAttribute("versions", versions);
    	
	    logInfo("versions - versions: {}", versions);
        
        
        model.addAttribute("documentView", documentViewService.findById(refId));
        
        model.addAttribute("pageTitle", "Renew Document ");
        return "fragments/renew_document"; // Adjust to your template
    }
    
    @PostMapping("/documents/html/renew")
    public ResponseEntity<Map<String, Object>> renewDocument(@Valid @ModelAttribute DocumentsDTO documentDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "view/documents/html");
        
        logInfo("data: {}", documentDTO);

        return handleRequest(result, () -> documentDataService.save(documentDTO), "Document renewed successfully", additionalData);
    }

    
    
    // Override to prevent ambiguity
    @Override
    @GetMapping("/documents/data/{id}")
    public ResponseEntity<DocumentsDTO> getById(@PathVariable Long id) {
        // You could optionally implement logic here or throw an exception
        throw new UnsupportedOperationException("Use the document view controller instead.");
    }
}
