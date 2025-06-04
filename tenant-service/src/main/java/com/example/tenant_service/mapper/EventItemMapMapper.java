// ─── 3) Mapper ────────────────────────────────────────────────────────────────

package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.EventItemMapDTO;
import com.example.tenant_service.entity.EventItemMap;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventItemMapMapper extends BaseMapper<EventItemMap, EventItemMapDTO> {
    EventItemMapMapper INSTANCE = Mappers.getMapper(EventItemMapMapper.class);
    
    // BaseMapper<E, D> already defines:
    //   EventItemMapDTO toDTO(EventItemMap entity);
    //   EventItemMap   toEntity(EventItemMapDTO dto);
    //
    // We’ll do relationship‐setting in the service, so no custom @Mapping here.
}
