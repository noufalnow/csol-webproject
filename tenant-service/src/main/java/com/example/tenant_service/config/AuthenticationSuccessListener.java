package com.example.tenant_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.service.CoreUserService;
import com.example.tenant_service.service.NodeService;
import com.example.tenant_service.util.SessionDebugHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationSuccessListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessListener.class);
    
    @Autowired
    private CoreUserService coreUserService;
    
    @Autowired
    private NodeService nodeService;

    @EventListener
    public void handleAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        try {
            Authentication authentication = event.getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) 
                RequestContextHolder.getRequestAttributes()).getRequest();
            
            String userEmail = authentication.getName();
            CoreUserDTO userDto = coreUserService.getUserByEmailAddress(userEmail);

            NodeDTO node = nodeService.findById(userDto.getUserNode());
                       
            //logger.error("Node details: {}", node); // This will use NodeDTO's toString()
            
            HttpSession session = request.getSession();
            session.setAttribute("ParentId", userDto.getUserNode());
            session.setAttribute("NODE_ID", userDto.getUserNode());
            session.setAttribute("USER_ID", userDto.getUserId());
            session.setAttribute("NODE_TYPE", node.getNodeType());
            session.setAttribute("USER_TYPE", userDto.getUserType());
            session.setAttribute("USER_NAME", userDto.getUserFname() + ' ' + userDto.getUserLname());
            session.setAttribute("NODE_NAME", node.getNodeName());
            
            SessionDebugHelper.logAllSessionAttributes(request.getSession());
            
        } catch (Exception e) {
            logger.error("Error during authentication success handling: {}", e.getMessage(), e);
        }
        
        
        
    }
}