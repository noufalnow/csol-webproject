package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.EventDTO;
import com.example.tenant_service.entity.Event;
import com.example.tenant_service.entity.Node;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper extends BaseMapper<Event, EventDTO> {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "hostNode.nodeId", target = "eventHostId")
    @Mapping(source = "hostNode.nodeName", target = "hostNodeName")
    EventDTO toDTO(Event event);

    @Mapping(source = "eventHostId", target = "hostNode")
    Event toEntity(EventDTO eventDTO);

    @Mapping(source = "eventHostId", target = "hostNode")
    void updateEventFromDto(EventDTO eventDTO, @MappingTarget Event event);

    default Node map(Long id) {
        if (id == null) {
            return null;
        }
        Node node = new Node();
        node.setNodeId(id);
        return node;
    }
}