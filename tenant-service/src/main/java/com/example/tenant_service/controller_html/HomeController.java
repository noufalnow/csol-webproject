package com.example.tenant_service.controller_html;

import java.util.Arrays;
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
import com.example.tenant_service.service.NodeService;
import com.example.tenant_service.config.SessionUtils;
import com.example.tenant_service.dto.EventDTO;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.lang.Tuple;

@Controller
public class HomeController {

	private final MisPropertyPayOptionHtmlController misPropertyPayOptionHtmlController;

	private final EventService eventService;

	@Autowired
	private SessionUtils sessionUtils;

	public HomeController(EventService eventService, HttpServletRequest request,
			MisPropertyPayOptionHtmlController misPropertyPayOptionHtmlController) {
		this.eventService = eventService;
		this.misPropertyPayOptionHtmlController = misPropertyPayOptionHtmlController;
	}

	@GetMapping("/home")
	public String home(Authentication authentication, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		long parentId = (Long) session.getAttribute("NODE_ID");
		long userId = (Long) session.getAttribute("USER_ID");

		List<Object[]> eventList = eventService.findByHostNodeHierarchy(parentId, userId);
		
		List<Object[]> resultList = eventService.getMemberEventsWithFilters(
	              null, null, userId, null, null, true);
		
		model.addAttribute("resultList", resultList);
		model.addAttribute("eventList", eventList);
		model.addAttribute("pageTitle", "Home - My Application");
		model.addAttribute("nodeType", session.getAttribute("NODE_TYPE"));
		model.addAttribute("userType",session.getAttribute("USER_TYPE").toString().trim());
		
		model.addAttribute("nodeName", session.getAttribute("NODE_NAME"));
		model.addAttribute("userName",session.getAttribute("USER_NAME"));

		if (session.getAttribute("USER_TYPE") != null
				&& "MEMBER".equals(session.getAttribute("USER_TYPE").toString().trim())) {
			model.addAttribute("content", "fragments/profile/profile");
		} else {
			model.addAttribute("content", "fragments/profile/official");
		}

		return "layout";
	}

}
