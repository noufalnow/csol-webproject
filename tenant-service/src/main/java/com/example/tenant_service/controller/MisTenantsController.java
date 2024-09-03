package com.example.tenant_service.controller;

import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.service.MisTenantsService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenants")
public class MisTenantsController extends BaseController<TenantDTO, MisTenantsService> {

    public MisTenantsController(MisTenantsService misTenantsService) {
        super(misTenantsService);
    }

    // Add any other MisTenantsController methods here if needed
}
