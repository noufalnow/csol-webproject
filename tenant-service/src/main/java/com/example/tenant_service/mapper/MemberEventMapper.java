package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.MemberEventDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.Event;
import com.example.tenant_service.entity.MemberEvent;
import com.example.tenant_service.entity.Node;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {CoreUserMapper.class, EventMapper.class, NodeMapper.class, ItemsMapper.class}
)
public interface MemberEventMapper extends BaseMapper<MemberEvent, MemberEventDTO> {

    @Override
    @Mapping(target = "memberId", source = "member.userId")
    @Mapping(
        target = "memberName",
        expression = "java(entity.getMember().getUserFname() + \" \" + entity.getMember().getUserLname())"
    )
    @Mapping(target = "meId", source = "meId")
    @Mapping(target = "memberNodeId", source = "memberNode.nodeId")
    @Mapping(target = "memberNodeName", source = "memberNode.nodeName")
    @Mapping(target = "eventId", source = "event.eventId")
    @Mapping(target = "nodeId", source = "node.nodeId")
    @Mapping(target = "approvedBy", source = "approvedBy.userId")
    @Mapping(target = "resultEntryBy", source = "resultEntryBy.userId")
    @Mapping(target = "resultApprovedBy", source = "resultApprovedBy.userId")
    @Mapping(target = "items", source = "items", qualifiedByName = "convertToDtoMap")
    @Mapping(target = "deleted", expression = "java(mapMemberEventDeletedToShort(entity.getDeleted()))")
    MemberEventDTO toDTO(MemberEvent entity);

    @Override
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "node", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "resultEntryBy", ignore = true)
    @Mapping(target = "resultApprovedBy", ignore = true)
    @Mapping(target = "memberNode", ignore = true)
    @Mapping(target = "items", source = "items", qualifiedByName = "convertToEntityMap")
    @Mapping(target = "deleted", expression = "java(mapShortToMemberEventDeleted(dto.getDeleted()))")
    MemberEvent toEntity(MemberEventDTO dto);

    /**
     * Very specific conversion methods for MemberEvent's deleted field only
     */
    default Short mapMemberEventDeletedToShort(Boolean deleted) {
        return deleted != null ? (short) (deleted ? 1 : 0) : null;
    }

    default Boolean mapShortToMemberEventDeleted(Short deleted) {
        return deleted != null ? deleted == 1 : null;
    }

    /**
     * For service.update: copy only non-relational, non-items fields.
     */
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "node", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "resultEntryBy", ignore = true)
    @Mapping(target = "resultApprovedBy", ignore = true)
    @Mapping(target = "memberNode", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(MemberEventDTO dto, @MappingTarget MemberEvent entity);

    @AfterMapping
    default void mapRelations(MemberEventDTO dto, @MappingTarget MemberEvent entity) {
        if (dto.getMemberId() != null) {
            CoreUser member = new CoreUser();
            member.setUserId(dto.getMemberId());
            entity.setMember(member);
        }
        if (dto.getMemberNodeId() != null) {
            Node memberNode = new Node();
            memberNode.setNodeId(dto.getMemberNodeId());
            entity.setMemberNode(memberNode);
        }
        if (dto.getEventId() != null) {
            Event event = new Event();
            event.setEventId(dto.getEventId());
            entity.setEvent(event);
        }
        if (dto.getNodeId() != null) {
            Node node = new Node();
            node.setNodeId(dto.getNodeId());
            entity.setNode(node);
        }
        if (dto.getApprovedBy() != null) {
            CoreUser approver = new CoreUser();
            approver.setUserId(dto.getApprovedBy());
            entity.setApprovedBy(approver);
        }
        if (dto.getResultEntryBy() != null) {
            CoreUser entryBy = new CoreUser();
            entryBy.setUserId(dto.getResultEntryBy());
            entity.setResultEntryBy(entryBy);
        }
        if (dto.getResultApprovedBy() != null) {
            CoreUser approvedBy = new CoreUser();
            approvedBy.setUserId(dto.getResultApprovedBy());
            entity.setResultApprovedBy(approvedBy);
        }
    }
}