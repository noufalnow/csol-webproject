package com.example.tenant_service.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.repository.CoreUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CoreUserRepository coreUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CoreUser user = coreUserRepository.findByUserEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getUserStatus() != 1) {
            throw new DisabledException("User account is inactive");
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUserEmail())
            .password(user.getUserPassword() != null ? user.getUserPassword() : "anyPassword")
            .authorities("ROLE_USER")
            .build();
    }

}