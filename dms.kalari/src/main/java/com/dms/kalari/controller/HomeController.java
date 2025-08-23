package com.dms.kalari.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dms.kalari.security.CustomUserPrincipal;
import com.dms.kalari.service.EventService;

@Controller
public class HomeController {

    private final MisPropertyPayOptionHtmlController misPropertyPayOptionHtmlController;
    private final EventService eventService;

    public HomeController(EventService eventService,
                          MisPropertyPayOptionHtmlController misPropertyPayOptionHtmlController) {
        this.eventService = eventService;
        this.misPropertyPayOptionHtmlController = misPropertyPayOptionHtmlController;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        // ✅ Get current authenticated user
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        long parentId = principal.getInstId();   // instead of NODE_ID
        long userId   = principal.getUserId();   // instead of USER_ID

        List<Object[]> eventList = eventService.findByHostNodeHierarchy(parentId, userId);
        List<Object[]> resultList = eventService.getMemberEventsWithFilters(
                null, null, userId, null, null, true, null);

        model.addAttribute("resultList", resultList);
        model.addAttribute("eventList", eventList);
        model.addAttribute("pageTitle", "Home ");

        // ✅ Populate attributes from principal
        model.addAttribute("nodeType", principal.getNodeType());
        model.addAttribute("userType", principal.getUserType().name());
        model.addAttribute("nodeName", principal.getNodeName());
        model.addAttribute("userName", principal.getUserFullName());

        if (principal.getUserType() != null 
                && principal.getUserType().name().equalsIgnoreCase("MEMBER")) {
            model.addAttribute("content", "fragments/admin/users/profile/profile");
        } else {
            model.addAttribute("content", "fragments/admin/users/profile/official");
        }

        return "layout";
    }
}
