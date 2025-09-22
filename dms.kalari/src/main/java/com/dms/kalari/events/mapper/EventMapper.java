package com.dms.kalari.events.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.events.dto.EventDTO;
import com.dms.kalari.events.entity.Event;
import com.dms.kalari.events.entity.MemberEvent;

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

    /**
     * Maps an Object array containing Event and MemberEvent to EventDTO.
     * Assumes that tuple[0] is Event and tuple[1] is MemberEvent.
     */
    default EventDTO toDTO(Object[] tuple) {
        if (tuple == null || tuple.length == 0) {
            return null;
        }

        Event event = (Event) tuple[0];
        MemberEvent memberEvent = (tuple.length > 1 && tuple[1] != null) ? (MemberEvent) tuple[1] : null;

        EventDTO dto = toDTO(event);
        // If EventDTO has fields that need to be set from MemberEvent, set them here.
        // For example:
        // if (memberEvent != null) {
        //     dto.setMemberSpecificField(memberEvent.getSomeField());
        // }

        return dto;
    }
}
