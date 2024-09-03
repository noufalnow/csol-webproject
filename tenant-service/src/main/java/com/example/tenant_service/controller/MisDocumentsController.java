package com.example.tenant_service.controller;

import com.example.tenant_service.dto.DocumentDTO;
import com.example.tenant_service.service.MisDocumentsService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class MisDocumentsController extends BaseController<DocumentDTO, MisDocumentsService> {

    public MisDocumentsController(MisDocumentsService misDocumentsService) {
        super(misDocumentsService);
    }

    // Add any other MisDocumentsController methods here if needed
}
