package com.dms.kalari.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.dto.EventItemDTO;
import com.dms.kalari.entity.EventItem;

@Mapper(componentModel = "spring")
public interface EventItemMapper extends BaseMapper<EventItem, EventItemDTO> {

    EventItemDTO toDTO(EventItem eventItem);

    EventItem toEntity(EventItemDTO eventItemDTO);

}
