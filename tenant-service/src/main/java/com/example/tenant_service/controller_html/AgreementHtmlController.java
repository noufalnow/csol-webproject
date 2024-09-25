package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.MisDocumentsService; // Adjust the service according to your structure
import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.common.BaseController;
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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import java.util.HashMap;

@Controller
public class AgreementHtmlController extends BaseController<DocumentDTO, MisDocumentsService> {

    // Inject service via constructor
    public AgreementHtmlController(MisDocumentsService documentsService) {
        super(documentsService);
    }

    @GetMapping("/documents/html")
    public String listDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "docId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model,
            @RequestParam Map<String, String> requestParams
    		) {
    	
        String decodeSearch = search;
        if (decodeSearch != null) {
            search = URLDecoder.decode(decodeSearch, StandardCharsets.UTF_8);
            requestParams.put("search", decodeSearch); // Replace the encoded value
        }
    	
    	
    	logInfo("Request Parameters: " + requestParams.toString());

        // Construct the Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

        logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}",
                page, size, sortField, sortDir, search);

        Page<DocumentDTO> documentPage = service.findAllPaginate(pageable, search);

        logInfo("Document Page - Total Elements: {}, Total Pages: {}", documentPage.getTotalElements(), documentPage.getTotalPages());
        logInfo("Documents: {}", documentPage.getContent());

        // Use the reusable method for setting up pagination
        setupPagination(model, documentPage, sortField, sortDir);

        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Document List - My Application");
        
        model.addAttribute("pageUrl",  "/documents/html");
        

        return "fragments/document_list"; // Change the view to your actual template
    }

    @GetMapping("/documents/html/{id}")
    public String viewDocumentById(@PathVariable Long id, Model model) {
        model.addAttribute("document", service.findById(id));
        model.addAttribute("pageTitle", "Document Detail - My Application");
        return "fragments/document_detail"; // Change the view to your actual template
    }

    @GetMapping("/documents/html/add")
    public String showAddDocumentForm(Model model) {
        model.addAttribute("pageTitle", "Add Document - My Application");
        model.addAttribute("document", new DocumentDTO());
        return "fragments/add_document"; // Change the view to your actual template
    }

    @PostMapping("/documents/html/add")
    public String addDocument(@Valid @ModelAttribute DocumentDTO documentDTO,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add Document - My Application");
            return "fragments/add_document"; // Return to form if there are validation errors
        }
        
        service.save(documentDTO);
        model.addAttribute("successMessage", "Document added successfully");
        return "redirect:/documents/html"; // Redirect after successful addition
    }

    @GetMapping("/documents/html/edit/{id}")
    public String editDocument(@PathVariable Long id, Model model) {
        model.addAttribute("document", service.findById(id));
        model.addAttribute("pageTitle", "Edit Document - My Application");
        return "fragments/edit_document"; // Change the view to your actual template
    }

    @PostMapping("/documents/html/update/{refId}")
    public String updateDocument(@PathVariable("refId") Long documentId,
                                  @Valid @ModelAttribute DocumentDTO documentDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("document", documentDTO);
            return "fragments/edit_document"; // Return to form if there are validation errors
        }

        service.update(documentId, documentDTO);
        model.addAttribute("successMessage", "Document updated successfully");
        return "redirect:/documents/html"; // Redirect after successful update
    }
}
