package com.example.tenant_service.common;

import java.util.Map;
import java.util.stream.Collectors;

public interface BaseMapper<Entity, DTO> {
    /**
     * Convert entity to DTO
     */
    DTO toDTO(Entity entity);

    /**
     * Convert DTO to entity
     */
    Entity toEntity(DTO dto);

    /**
     * Convert Boolean to Short (1 for true, 0 for false)
     */
    default Short map(Boolean value) {
        return (value != null && value) ? (short) 1 : (short) 0;
    }

    /**
     * Convert Short to Boolean (true if 1)
     */
    default Boolean map(Short value) {
        return (value != null && value == 1);
    }

    /**
     * Convert DTO items map (Integer keys) to entity items map (String keys)
     */
    default Map<String, String> convertItemsToStringMap(Map<Integer, String> items) {
        if (items == null) {
            return null;
        }
        return items.entrySet().stream()
            .collect(Collectors.toMap(
                e -> e.getKey().toString(),
                Map.Entry::getValue
            ));
    }

    /**
     * Convert entity items map (String keys) to DTO items map (Integer keys)
     */
    default Map<Integer, String> convertItemsToIntegerMap(Map<String, String> items) {
        if (items == null) {
            return null;
        }
        return items.entrySet().stream()
            .collect(Collectors.toMap(
                e -> Integer.parseInt(e.getKey()),
                Map.Entry::getValue
            ));
    }
}


