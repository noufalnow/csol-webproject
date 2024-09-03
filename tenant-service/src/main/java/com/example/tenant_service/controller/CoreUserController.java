package com.example.tenant_service.controller;

import com.example.tenant_service.dto.CoreUserDTO;
import com.example.tenant_service.service.CoreUserService;
import com.example.tenant_service.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class CoreUserController extends BaseController<CoreUserDTO, CoreUserService> {

    public CoreUserController(CoreUserService coreUserService) {
        super(coreUserService);
    }

    // Add any other CoreUserController methods here if needed
}
