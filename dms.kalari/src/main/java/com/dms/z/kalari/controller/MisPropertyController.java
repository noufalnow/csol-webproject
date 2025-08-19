package com.dms.z.kalari.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.kalari.common.BaseController;
import com.dms.kalari.dto.PropertyDTO;
import com.dms.kalari.service.MisPropertyService;

@RestController
@RequestMapping("/properties")
public class MisPropertyController extends BaseController<PropertyDTO, MisPropertyService> {

    public MisPropertyController(MisPropertyService misPropertyService) {
        super(misPropertyService);
    }

    // Add any other MisPropertyController methods here if needed
}
