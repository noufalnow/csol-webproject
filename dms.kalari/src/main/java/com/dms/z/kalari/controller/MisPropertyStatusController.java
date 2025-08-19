package com.dms.kalari.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.PropertyStatusDTO;
import com.dms.kalari.service.MisPropertyStatusService;

@RestController
@RequestMapping("/property-statuses")
public class MisPropertyStatusController extends BaseController<PropertyStatusDTO, MisPropertyStatusService> {

    public MisPropertyStatusController(MisPropertyStatusService misPropertyStatusService) {
        super(misPropertyStatusService);
    }

    // Add any other MisPropertyStatusController methods here if needed
}
