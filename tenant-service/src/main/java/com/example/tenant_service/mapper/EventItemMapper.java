package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.EventItemDTO;
import com.example.tenant_service.entity.EventItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventItemMapper extends BaseMapper<EventItem, EventItemDTO> {

    EventItemDTO toDTO(EventItem eventItem);

    EventItem toEntity(EventItemDTO eventItemDTO);

}
