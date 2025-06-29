package com.example.tenant_service.controller_html;

import com.example.tenant_service.dto.DocumentsDTO;
import com.example.tenant_service.dto.DocumentsViewDTO;
import com.example.tenant_service.service.MisDocumentViewService;
import com.example.tenant_service.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/view")
public class MisDocumentViewController extends BaseController<DocumentsViewDTO, MisDocumentViewService> {

    private final MisDocumentViewService documentViewService;

    @Autowired
    public MisDocumentViewController(MisDocumentViewService documentViewService) {
        super(documentViewService);
        this.documentViewService = documentViewService;
    }

    @GetMapping("/documents/html")
    public String listDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "docId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model,
            @RequestParam Map<String, String> requestParams) {

        if (search != null) {
            search = URLDecoder.decode(search, StandardCharsets.UTF_8);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
        Page<DocumentsViewDTO> documentPage = documentViewService.findAllPaginate(pageable, search);

        setupPagination(model, documentPage, sortField, sortDir);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Document List ");
        model.addAttribute("pageUrl",  "/view/documents/html");

        return "fragments/document_list"; // Adjust to your template
    }

    @GetMapping("/documents/html/{id}")
    public String viewDocumentById(@PathVariable Long id, Model model) {
    	
    	DocumentsViewDTO document  = documentViewService.findById(id);
    	
        model.addAttribute("document", document);
        
        List<DocumentsDTO> versions = documentViewService.findAllByDocAgreement(document.getDocAgreement(),id);
                
        model.addAttribute("versions", versions);

        
        model.addAttribute("pageTitle", "Document Detail ");
        return "fragments/document_details"; // Adjust to your template
    }
    
    
    
}
