package com.example.tenant_service.controller_html;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.tenant_service.service.CoreUserService;
import com.example.tenant_service.service.EventService;
import com.example.tenant_service.dto.EventDTO;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {
	
    private final CoreUserService coreUserService;
    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    

    @Autowired  // Constructor injection (recommended)
    public HomeController(CoreUserService coreUserService, EventService eventService) {
        this.coreUserService = coreUserService;
        this.eventService = eventService;
    }

	@GetMapping("/home")
    public String home(Authentication authentication, Model model, HttpServletRequest request) {
		
        String userEmail = authentication.getName();
        CoreUserDTO userDto  = coreUserService.getUserByEmailAddress(userEmail);
        
        HttpSession session = request.getSession();
        session.setAttribute("ParentId", userDto.getUserNode());
        session.setAttribute("USER_ID", userDto.getUserId());
		
		
        //List<EventDTO> eventList  = eventService.findByHostNode(userDto.getUserNode()); 
        //model.addAttribute("eventList", eventList);
        
        List<Object[]> eventList = eventService.findByHostNodeHierarchy(userDto.getUserNode(), userDto.getUserId());
        model.addAttribute("eventList", eventList);
        
        

		
        model.addAttribute("pageTitle", "Home - My Application");
        model.addAttribute("content", "fragments/profile/profile");
        
        


        return "layout";
    }

}

