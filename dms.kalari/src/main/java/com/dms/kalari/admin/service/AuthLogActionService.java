package com.dms.kalari.admin.service;

import com.dms.kalari.admin.entity.AuthLogAction;
import com.dms.kalari.repository.AuthLogActionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthLogActionService {

    private final AuthLogActionRepository repository;
    private final ObjectMapper objectMapper;

    private static final Set<String> SENSITIVE_PARAM_KEYS = Set.of(
        "_csrf", "password", "secretKey", "jwt", "token", "authorization"
    );

    public AuthLogActionService(AuthLogActionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Record request-time action (parameters + access decision).
     */
    public void logPreAction(Long loginId, String requestUri, String httpMethod, Map<String, ?> params, boolean accessGranted) {
        AuthLogAction log = new AuthLogAction();
        log.setLoginId(loginId);
        log.setRequestUri(requestUri);

        try {
            Map<String, Object> wrapper = Map.of(
                "method", httpMethod,
                "params", sanitizeParams(params),
                "accessGranted", accessGranted,
                "phase", "PRE"
            );
            log.setRequestData(objectMapper.writeValueAsString(wrapper));
        } catch (Exception e) {
            log.setRequestData("{\"accessGranted\":" + accessGranted +
                               ",\"phase\":\"PRE\",\"method\":\"" + httpMethod + "\"}");
        }

        repository.save(log);
    }

    /**
     * Record final persisted state of an entity after modification.
     */
    public void logPostAction(Long loginId, String requestUri, String httpMethod, Object entity) {
        AuthLogAction log = new AuthLogAction();
        log.setLoginId(loginId);
        log.setRequestUri(requestUri);

        try {
            var wrapper = Map.of(
                "method", httpMethod,
                "entityType", entity != null ? entity.getClass().getSimpleName() : "null",
                "entityDump", sanitizeEntity(entity),
                "phase", "POST"
            );
            log.setRequestData(objectMapper.writeValueAsString(wrapper));
        } catch (Exception e) {
            log.setRequestData("{\"phase\":\"POST\", \"error\":\"serialize-failed\",\"method\":\"" + httpMethod + "\"}");
        }

        repository.save(log);
    }

    /**
     * Remove sensitive fields from request parameters.
     */
    private Map<String, ?> sanitizeParams(Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> cleaned = new HashMap<>();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (!SENSITIVE_PARAM_KEYS.contains(entry.getKey().toLowerCase())) {
                cleaned.put(entry.getKey(), entry.getValue());
            }
        }
        return cleaned;
    }

    /**
     * Sanitize entity before logging (remove sensitive fields).
     */
    private Object sanitizeEntity(Object entity) {
        if (entity == null) {
            return Collections.emptyMap();
        }
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> rawMap = objectMapper.convertValue(entity, Map.class);

            for (String key : SENSITIVE_PARAM_KEYS) {
                rawMap.remove(key);
            }
            return rawMap;
        } catch (Exception e) {
            return Map.of("error", "failed-to-convert-entity");
        }
    }
}
