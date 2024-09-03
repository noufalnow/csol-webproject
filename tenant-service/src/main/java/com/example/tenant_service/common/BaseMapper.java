package com.example.tenant_service.common;

public interface BaseMapper<Entity, DTO> {
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
    default Short map(Boolean value) {
        return value != null && value ? (short) 1 : (short) 0;
    }

    default Boolean map(Short value) {
        return value != null && value == 1;
    }
}



