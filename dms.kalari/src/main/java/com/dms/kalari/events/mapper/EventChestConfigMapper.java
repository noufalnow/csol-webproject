package com.dms.kalari.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.events.dto.EventChestConfigDTO;
import com.dms.kalari.events.entity.EventChestConfig;

@Mapper(componentModel = "spring")
public interface EventChestConfigMapper
        extends BaseMapper<EventChestConfig, EventChestConfigDTO> {

    @Mapping(source = "eventItemMap.eimId", target = "eventItemMapId")
    @Mapping(source = "eventItemMap.event.eventId", target = "eventId")
    @Mapping(source = "eventItemMap.item.evitemId", target = "itemId")
    @Mapping(source = "eventItemMap.item.evitemName", target = "itemName")
    @Mapping(source = "eventItemMap.category", target = "category")
    EventChestConfigDTO toDTO(EventChestConfig entity);

    @Mapping(target = "eventItemMap", ignore = true)
    EventChestConfig toEntity(EventChestConfigDTO dto);
}