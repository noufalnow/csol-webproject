package com.example.tenant_service.controller;

import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.service.MisPropertyService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
public class MisPropertyController extends BaseController<PropertyDTO, MisPropertyService> {

    public MisPropertyController(MisPropertyService misPropertyService) {
        super(misPropertyService);
    }

    // Add any other MisPropertyController methods here if needed
}
