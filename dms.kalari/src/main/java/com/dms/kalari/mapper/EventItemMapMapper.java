// ─── 3) Mapper ────────────────────────────────────────────────────────────────

package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.EventItemMapDTO;
import com.dms.kalari.entity.EventItemMap;

@Mapper(componentModel = "spring")
public interface EventItemMapMapper extends BaseMapper<EventItemMap, EventItemMapDTO> {
    EventItemMapMapper INSTANCE = Mappers.getMapper(EventItemMapMapper.class);
    
    // BaseMapper<E, D> already defines:
    //   EventItemMapDTO toDTO(EventItemMap entity);
    //   EventItemMap   toEntity(EventItemMapDTO dto);
    //
    // We’ll do relationship‐setting in the service, so no custom @Mapping here.
}
