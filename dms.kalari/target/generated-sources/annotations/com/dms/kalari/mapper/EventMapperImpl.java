package com.dms.kalari.mapper;

import com.dms.kalari.dto.EventDTO;
import com.dms.kalari.dto.NodeDTO;
import com.dms.kalari.entity.Event;
import com.dms.kalari.entity.Node;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDTO toDTO(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();

        eventDTO.setEventHostId( eventHostNodeNodeId( event ) );
        eventDTO.setHostNodeName( eventHostNodeNodeName( event ) );
        eventDTO.setActive( event.getActive() );
        eventDTO.setDeleted( map( event.getDeleted() ) );
        eventDTO.setTCreated( event.getTCreated() );
        eventDTO.setTDeleted( event.getTDeleted() );
        eventDTO.setTModified( event.getTModified() );
        eventDTO.setUCreated( event.getUCreated() );
        eventDTO.setUDeleted( event.getUDeleted() );
        eventDTO.setUModified( event.getUModified() );
        eventDTO.setEventHost( typeToType( event.getEventHost() ) );
        eventDTO.setEventId( event.getEventId() );
        eventDTO.setEventName( event.getEventName() );
        eventDTO.setEventOfficialPhone( event.getEventOfficialPhone() );
        eventDTO.setEventPeriodEnd( event.getEventPeriodEnd() );
        eventDTO.setEventPeriodStart( event.getEventPeriodStart() );
        eventDTO.setEventVenue( event.getEventVenue() );
        eventDTO.setEventYear( event.getEventYear() );
        eventDTO.setHostNode( nodeToNodeDTO( event.getHostNode() ) );

        return eventDTO;
    }

    @Override
    public Event toEntity(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setHostNode( map( eventDTO.getEventHostId() ) );
        event.setActive( eventDTO.getActive() );
        event.setDeleted( map( eventDTO.getDeleted() ) );
        event.setTCreated( eventDTO.getTCreated() );
        event.setTDeleted( eventDTO.getTDeleted() );
        event.setTModified( eventDTO.getTModified() );
        event.setUCreated( eventDTO.getUCreated() );
        event.setUDeleted( eventDTO.getUDeleted() );
        event.setUModified( eventDTO.getUModified() );
        event.setEventHost( typeToType1( eventDTO.getEventHost() ) );
        event.setEventId( eventDTO.getEventId() );
        event.setEventName( eventDTO.getEventName() );
        event.setEventOfficialPhone( eventDTO.getEventOfficialPhone() );
        event.setEventPeriodEnd( eventDTO.getEventPeriodEnd() );
        event.setEventPeriodStart( eventDTO.getEventPeriodStart() );
        event.setEventVenue( eventDTO.getEventVenue() );
        event.setEventYear( eventDTO.getEventYear() );

        return event;
    }

    @Override
    public void updateEventFromDto(EventDTO eventDTO, Event event) {
        if ( eventDTO == null ) {
            return;
        }

        if ( eventDTO.getEventHostId() != null ) {
            event.setHostNode( map( eventDTO.getEventHostId() ) );
        }
        if ( eventDTO.getActive() != null ) {
            event.setActive( eventDTO.getActive() );
        }
        if ( eventDTO.getDeleted() != null ) {
            event.setDeleted( map( eventDTO.getDeleted() ) );
        }
        if ( eventDTO.getTCreated() != null ) {
            event.setTCreated( eventDTO.getTCreated() );
        }
        if ( eventDTO.getTDeleted() != null ) {
            event.setTDeleted( eventDTO.getTDeleted() );
        }
        if ( eventDTO.getTModified() != null ) {
            event.setTModified( eventDTO.getTModified() );
        }
        if ( eventDTO.getUCreated() != null ) {
            event.setUCreated( eventDTO.getUCreated() );
        }
        if ( eventDTO.getUDeleted() != null ) {
            event.setUDeleted( eventDTO.getUDeleted() );
        }
        if ( eventDTO.getUModified() != null ) {
            event.setUModified( eventDTO.getUModified() );
        }
        if ( eventDTO.getEventHost() != null ) {
            event.setEventHost( typeToType1( eventDTO.getEventHost() ) );
        }
        if ( eventDTO.getEventId() != null ) {
            event.setEventId( eventDTO.getEventId() );
        }
        if ( eventDTO.getEventName() != null ) {
            event.setEventName( eventDTO.getEventName() );
        }
        if ( eventDTO.getEventOfficialPhone() != null ) {
            event.setEventOfficialPhone( eventDTO.getEventOfficialPhone() );
        }
        if ( eventDTO.getEventPeriodEnd() != null ) {
            event.setEventPeriodEnd( eventDTO.getEventPeriodEnd() );
        }
        if ( eventDTO.getEventPeriodStart() != null ) {
            event.setEventPeriodStart( eventDTO.getEventPeriodStart() );
        }
        if ( eventDTO.getEventVenue() != null ) {
            event.setEventVenue( eventDTO.getEventVenue() );
        }
        if ( eventDTO.getEventYear() != null ) {
            event.setEventYear( eventDTO.getEventYear() );
        }
    }

    private Long eventHostNodeNodeId(Event event) {
        if ( event == null ) {
            return null;
        }
        Node hostNode = event.getHostNode();
        if ( hostNode == null ) {
            return null;
        }
        Long nodeId = hostNode.getNodeId();
        if ( nodeId == null ) {
            return null;
        }
        return nodeId;
    }

    private String eventHostNodeNodeName(Event event) {
        if ( event == null ) {
            return null;
        }
        Node hostNode = event.getHostNode();
        if ( hostNode == null ) {
            return null;
        }
        String nodeName = hostNode.getNodeName();
        if ( nodeName == null ) {
            return null;
        }
        return nodeName;
    }

    protected Node.Type typeToType(Event.Type type) {
        if ( type == null ) {
            return null;
        }

        Node.Type type1;

        switch ( type ) {
            case ROOT: type1 = Node.Type.ROOT;
            break;
            case COUNTRY: type1 = Node.Type.COUNTRY;
            break;
            case STATE: type1 = Node.Type.STATE;
            break;
            case DISTRICT: type1 = Node.Type.DISTRICT;
            break;
            case KALARI: type1 = Node.Type.KALARI;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + type );
        }

        return type1;
    }

    protected List<NodeDTO> nodeListToNodeDTOList(List<Node> list) {
        if ( list == null ) {
            return null;
        }

        List<NodeDTO> list1 = new ArrayList<NodeDTO>( list.size() );
        for ( Node node : list ) {
            list1.add( nodeToNodeDTO( node ) );
        }

        return list1;
    }

    protected NodeDTO nodeToNodeDTO(Node node) {
        if ( node == null ) {
            return null;
        }

        NodeDTO nodeDTO = new NodeDTO();

        nodeDTO.setActive( node.getActive() );
        nodeDTO.setDeleted( map( node.getDeleted() ) );
        nodeDTO.setTCreated( node.getTCreated() );
        nodeDTO.setTDeleted( node.getTDeleted() );
        nodeDTO.setTModified( node.getTModified() );
        nodeDTO.setUCreated( node.getUCreated() );
        nodeDTO.setUDeleted( node.getUDeleted() );
        nodeDTO.setUModified( node.getUModified() );
        nodeDTO.setChildren( nodeListToNodeDTOList( node.getChildren() ) );
        nodeDTO.setNodeId( node.getNodeId() );
        nodeDTO.setNodeName( node.getNodeName() );
        nodeDTO.setNodeStatus( node.getNodeStatus() );
        nodeDTO.setNodeType( node.getNodeType() );

        return nodeDTO;
    }

    protected Event.Type typeToType1(Node.Type type) {
        if ( type == null ) {
            return null;
        }

        Event.Type type1;

        switch ( type ) {
            case ROOT: type1 = Event.Type.ROOT;
            break;
            case COUNTRY: type1 = Event.Type.COUNTRY;
            break;
            case STATE: type1 = Event.Type.STATE;
            break;
            case DISTRICT: type1 = Event.Type.DISTRICT;
            break;
            case KALARI: type1 = Event.Type.KALARI;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + type );
        }

        return type1;
    }
}
