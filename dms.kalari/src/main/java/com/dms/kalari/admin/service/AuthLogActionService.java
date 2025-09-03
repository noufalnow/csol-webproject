package com.dms.kalari.admin.service;

import com.dms.kalari.admin.entity.AuthLogAction;
import com.dms.kalari.repository.AuthLogActionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthLogActionService {

    private final AuthLogActionRepository repository;
    private final ObjectMapper objectMapper;

    public AuthLogActionService(AuthLogActionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Log only for GET requests (pre + decision).
     */
    public void logPreAction(Long loginId, String requestUri, String httpMethod, Map<String, ?> params, boolean accessGranted) {
        if (!"GET".equalsIgnoreCase(httpMethod)) {
            // skip PRE logs for POST/PUT/DELETE
            return;
        }

        AuthLogAction log = new AuthLogAction();
        log.setLoginId(loginId);
        log.setRequestUri(requestUri);

        try {
            Map<String, Object> wrapper = Map.of(
                "method", httpMethod,
                "params", params != null ? params : Collections.emptyMap(),
                "accessGranted", accessGranted,
                "phase", "PRE"
            );
            log.setRequestData(objectMapper.writeValueAsString(wrapper));
        } catch (Exception e) {
            log.setRequestData("{\"accessGranted\":" + accessGranted + ",\"phase\":\"PRE\",\"method\":\"" + httpMethod + "\"}");
        }

        repository.save(log);
    }

    /**
     * Log final state only if successful (2xx/3xx responses).
     */
    public void logPostAction(Long loginId, String requestUri, String httpMethod, Object entity) {
        AuthLogAction log = new AuthLogAction();
        log.setLoginId(loginId);
        log.setRequestUri(requestUri);

        try {
            Map<String, Object> wrapper = new LinkedHashMap<>();
            wrapper.put("method", httpMethod);
            wrapper.put("phase", "POST");
            
            if (entity instanceof Map) {
                wrapper.put("entityDump", entity); // Already sanitized
            } else {
                wrapper.put("entityDump", entity); // For other types
            }

            log.setRequestData(objectMapper.writeValueAsString(wrapper));
        } catch (Exception e) {
            log.setRequestData("{\"phase\":\"POST\", \"error\":\"serialize-failed\",\"method\":\"" + httpMethod + "\"}");
        }

        repository.save(log);
    }



}
