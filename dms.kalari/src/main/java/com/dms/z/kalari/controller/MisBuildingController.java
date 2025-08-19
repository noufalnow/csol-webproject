package com.dms.kalari.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.BuildingDTO;
import com.dms.kalari.service.MisBuildingService;

@RestController
@RequestMapping("/buildings")
public class MisBuildingController extends BaseController<BuildingDTO, MisBuildingService> {

    public MisBuildingController(MisBuildingService misBuildingService) {
        super(misBuildingService);
    }

    // Add any other MisBuildingController methods here if needed
}
