package com.example.tenant_service.controller;

import com.example.tenant_service.dto.BuildingDTO;
import com.example.tenant_service.service.MisBuildingService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buildings")
public class MisBuildingController extends BaseController<BuildingDTO, MisBuildingService> {

    public MisBuildingController(MisBuildingService misBuildingService) {
        super(misBuildingService);
    }

    // Add any other MisBuildingController methods here if needed
}
