package com.dms.kalari.admin.service;

import com.dms.kalari.admin.entity.AuthLoginLog;
import com.dms.kalari.repository.AuthLoginLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class AuthLoginLogService {

    private final AuthLoginLogRepository repository;

    public AuthLoginLogService(AuthLoginLogRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a login record and return the generated id.
     * Safe to call multiple times; calling code should check/set principal.loginId to avoid duplicates.
     */
    @Transactional
    public Long addLoginLog(Long userId, HttpServletRequest request) {
        AuthLoginLog log = new AuthLoginLog();
        log.setUserId(userId);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setSessionId(request.getSession().getId());

        AuthLoginLog saved = repository.save(log);
        return saved.getId();
    }
}
