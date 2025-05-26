package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.MemberEventDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.Event;
import com.example.tenant_service.entity.MemberEvent;
import com.example.tenant_service.entity.Node;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	    componentModel = "spring",
	    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	    uses = {CoreUserMapper.class, EventMapper.class, NodeMapper.class}
	)
	public interface MemberEventMapper extends BaseMapper<MemberEvent, MemberEventDTO> {

    /**
     * Alias so service calls toDto(...) compile.
     */
    default MemberEventDTO toDto(MemberEvent entity) {
        return toDTO(entity);
    }

    @Override
    @Mapping(target = "memberId", source = "member.userId")
    @Mapping(target = "eventId", source = "event.eventId")
    @Mapping(target = "nodeId", source = "node.nodeId")
    @Mapping(target = "approvedBy", source = "approvedBy.userId")
    @Mapping(target = "resultEntryBy", source = "resultEntryBy.userId")
    @Mapping(target = "resultApprovedBy", source = "resultApprovedBy.userId")
    @Mapping(target = "items", expression = "java(convertItemsToIntegerMap(entity.getItems()))")
    @Mapping(target = "deleted", ignore = true)
    MemberEventDTO toDTO(MemberEvent entity);

    @Override
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "node", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "resultEntryBy", ignore = true)
    @Mapping(target = "resultApprovedBy", ignore = true)
    @Mapping(target = "items", expression = "java(convertItemsToStringMap(dto.getItems()))")
    @Mapping(target = "deleted", ignore = true)
    MemberEvent toEntity(MemberEventDTO dto);

    /**
     * For service.update: copy only non-relational, non-items fields.
     */
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "node", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "resultEntryBy", ignore = true)
    @Mapping(target = "resultApprovedBy", ignore = true)
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
    
    public class KalariItemMapper {

        // Convert entity's Map<String, String> to DTO's Map<Integer, Integer>
        public static Map<Integer, Integer> convertToDtoMap(Map<String, String> entityMap) {
            if (entityMap == null) return new HashMap<>();
            
            Map<Integer, Integer> dtoMap = new HashMap<>();
            entityMap.forEach((key, value) -> {
                try {
                    int intKey = Integer.parseInt(key);
                    Integer intValue = value != null ? Integer.parseInt(value) : null;
                    dtoMap.put(intKey, intValue);
                } catch (NumberFormatException e) {
                    // Handle or log invalid entries
                }
            });
            return dtoMap;
        }

        // Convert DTO's Map<Integer, Integer> to entity's Map<String, String>
        public static Map<String, String> convertToEntityMap(Map<Integer, Integer> dtoMap) {
            if (dtoMap == null) return new HashMap<>();
            
            Map<String, String> entityMap = new HashMap<>();
            dtoMap.forEach((key, value) -> {
                entityMap.put(key.toString(), value != null ? value.toString() : null);
            });
            return entityMap;
        }
    }
    
}
