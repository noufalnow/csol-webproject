package com.dms.kalari.mapper;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.dto.MemberEventDTO;
import com.dms.kalari.entity.Event;
import com.dms.kalari.entity.MemberEvent;
import com.dms.kalari.entity.Node;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:31+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MemberEventMapperImpl implements MemberEventMapper {

    @Autowired
    private ItemsMapper itemsMapper;

    @Override
    public MemberEventDTO toDTO(MemberEvent entity) {
        if ( entity == null ) {
            return null;
        }

        MemberEventDTO memberEventDTO = new MemberEventDTO();

        memberEventDTO.setMemberId( entityMemberUserId( entity ) );
        memberEventDTO.setMeId( entity.getMeId() );
        memberEventDTO.setMemberNodeId( entityMemberNodeNodeId( entity ) );
        memberEventDTO.setMemberNodeName( entityMemberNodeNodeName( entity ) );
        memberEventDTO.setEventId( entityEventEventId( entity ) );
        memberEventDTO.setNodeId( entityNodeNodeId( entity ) );
        memberEventDTO.setApprovedBy( entityApprovedByUserId( entity ) );
        memberEventDTO.setResultEntryBy( entityResultEntryByUserId( entity ) );
        memberEventDTO.setResultApprovedBy( entityResultApprovedByUserId( entity ) );
        memberEventDTO.setItems( itemsMapper.convertToDtoMap( entity.getItems() ) );
        memberEventDTO.setActive( entity.getActive() );
        memberEventDTO.setTCreated( entity.getTCreated() );
        memberEventDTO.setTDeleted( entity.getTDeleted() );
        memberEventDTO.setTModified( entity.getTModified() );
        memberEventDTO.setUCreated( entity.getUCreated() );
        memberEventDTO.setUDeleted( entity.getUDeleted() );
        memberEventDTO.setUModified( entity.getUModified() );
        memberEventDTO.setApplyDate( entity.getApplyDate() );
        memberEventDTO.setApprovedDate( entity.getApprovedDate() );
        memberEventDTO.setResultApprovalDate( entity.getResultApprovalDate() );
        memberEventDTO.setResultDate( entity.getResultDate() );

        memberEventDTO.setMemberName( entity.getMember().getUserFname() + " " + entity.getMember().getUserLname() );
        memberEventDTO.setDeleted( mapMemberEventDeletedToShort(entity.getDeleted()) );

        return memberEventDTO;
    }

    @Override
    public MemberEvent toEntity(MemberEventDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MemberEvent memberEvent = new MemberEvent();

        memberEvent.setItems( itemsMapper.convertToEntityMap( dto.getItems() ) );
        memberEvent.setActive( dto.getActive() );
        memberEvent.setTCreated( dto.getTCreated() );
        memberEvent.setTDeleted( dto.getTDeleted() );
        memberEvent.setTModified( dto.getTModified() );
        memberEvent.setUCreated( dto.getUCreated() );
        memberEvent.setUDeleted( dto.getUDeleted() );
        memberEvent.setUModified( dto.getUModified() );
        memberEvent.setApplyDate( dto.getApplyDate() );
        memberEvent.setApprovedDate( dto.getApprovedDate() );
        memberEvent.setMeId( dto.getMeId() );
        memberEvent.setResultApprovalDate( dto.getResultApprovalDate() );
        memberEvent.setResultDate( dto.getResultDate() );

        memberEvent.setDeleted( mapShortToMemberEventDeleted(dto.getDeleted()) );

        mapRelations( dto, memberEvent );

        return memberEvent;
    }

    @Override
    public void updateEntityFromDto(MemberEventDTO dto, MemberEvent entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getActive() != null ) {
            entity.setActive( dto.getActive() );
        }
        if ( dto.getTCreated() != null ) {
            entity.setTCreated( dto.getTCreated() );
        }
        if ( dto.getTDeleted() != null ) {
            entity.setTDeleted( dto.getTDeleted() );
        }
        if ( dto.getTModified() != null ) {
            entity.setTModified( dto.getTModified() );
        }
        if ( dto.getUCreated() != null ) {
            entity.setUCreated( dto.getUCreated() );
        }
        if ( dto.getUDeleted() != null ) {
            entity.setUDeleted( dto.getUDeleted() );
        }
        if ( dto.getUModified() != null ) {
            entity.setUModified( dto.getUModified() );
        }
        if ( dto.getApplyDate() != null ) {
            entity.setApplyDate( dto.getApplyDate() );
        }
        if ( dto.getApprovedDate() != null ) {
            entity.setApprovedDate( dto.getApprovedDate() );
        }
        if ( dto.getMeId() != null ) {
            entity.setMeId( dto.getMeId() );
        }
        if ( dto.getResultApprovalDate() != null ) {
            entity.setResultApprovalDate( dto.getResultApprovalDate() );
        }
        if ( dto.getResultDate() != null ) {
            entity.setResultDate( dto.getResultDate() );
        }

        mapRelations( dto, entity );
    }

    private Long entityMemberUserId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        CoreUser member = memberEvent.getMember();
        if ( member == null ) {
            return null;
        }
        Long userId = member.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long entityMemberNodeNodeId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        Node memberNode = memberEvent.getMemberNode();
        if ( memberNode == null ) {
            return null;
        }
        Long nodeId = memberNode.getNodeId();
        if ( nodeId == null ) {
            return null;
        }
        return nodeId;
    }

    private String entityMemberNodeNodeName(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        Node memberNode = memberEvent.getMemberNode();
        if ( memberNode == null ) {
            return null;
        }
        String nodeName = memberNode.getNodeName();
        if ( nodeName == null ) {
            return null;
        }
        return nodeName;
    }

    private Long entityEventEventId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        Event event = memberEvent.getEvent();
        if ( event == null ) {
            return null;
        }
        Long eventId = event.getEventId();
        if ( eventId == null ) {
            return null;
        }
        return eventId;
    }

    private Long entityNodeNodeId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        Node node = memberEvent.getNode();
        if ( node == null ) {
            return null;
        }
        Long nodeId = node.getNodeId();
        if ( nodeId == null ) {
            return null;
        }
        return nodeId;
    }

    private Long entityApprovedByUserId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        CoreUser approvedBy = memberEvent.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        Long userId = approvedBy.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long entityResultEntryByUserId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        CoreUser resultEntryBy = memberEvent.getResultEntryBy();
        if ( resultEntryBy == null ) {
            return null;
        }
        Long userId = resultEntryBy.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long entityResultApprovedByUserId(MemberEvent memberEvent) {
        if ( memberEvent == null ) {
            return null;
        }
        CoreUser resultApprovedBy = memberEvent.getResultApprovedBy();
        if ( resultApprovedBy == null ) {
            return null;
        }
        Long userId = resultApprovedBy.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
