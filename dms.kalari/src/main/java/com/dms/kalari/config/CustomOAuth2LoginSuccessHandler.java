package com.dms.kalari.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dms.kalari.security.CustomUserDetailsService;
import com.dms.kalari.security.CustomUserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public CustomOAuth2LoginSuccessHandler(@Lazy CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. Get OAuth2User
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        System.out.println("OAuth2 email: " + email);

        // 2. Load your user details from DB
        CustomUserPrincipal userDetails = (CustomUserPrincipal) userDetailsService.loadUserByUsername(email);

        // 3. Create a new Authentication like form login
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 4. Set it into SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Redirect
        response.sendRedirect("/home");
    }
}

