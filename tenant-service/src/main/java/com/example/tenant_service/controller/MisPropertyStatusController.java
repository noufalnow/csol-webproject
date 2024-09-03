package com.example.tenant_service.controller;

import com.example.tenant_service.dto.PropertyStatusDTO;
import com.example.tenant_service.service.MisPropertyStatusService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property-statuses")
public class MisPropertyStatusController extends BaseController<PropertyStatusDTO, MisPropertyStatusService> {

    public MisPropertyStatusController(MisPropertyStatusService misPropertyStatusService) {
        super(misPropertyStatusService);
    }

    // Add any other MisPropertyStatusController methods here if needed
}
