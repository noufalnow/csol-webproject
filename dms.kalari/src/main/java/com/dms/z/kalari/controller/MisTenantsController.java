package com.dms.z.kalari.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.TenantDTO;
import com.dms.kalari.service.MisTenantsService;

@RestController
@RequestMapping("/tenants")
public class MisTenantsController extends BaseController<TenantDTO, MisTenantsService> {

    public MisTenantsController(MisTenantsService misTenantsService) {
        super(misTenantsService);
    }

    // Add any other MisTenantsController methods here if needed
}
