package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.entity.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NodeMapper extends BaseMapper<Node, NodeDTO> {
    NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);

    @Mapping(source = "parent.nodeId", target = "parentId")
    @Mapping(source = "parent.nodeName", target = "parentName")
    @Mapping(source = "nodeType", target = "nodeTypeLabel")
    NodeDTO toDTO(Node node);

    @Mapping(target = "parent", expression = "java(mapParentIdToNode(dto.getParentId()))")
    Node toEntity(NodeDTO dto);

    default Node mapParentIdToNode(Long parentId) {
        if (parentId == null) {
            return null;
        }
        Node parent = new Node();
        parent.setNodeId(parentId);
        return parent;
    }

    void updateNodeFromDto(NodeDTO nodeDTO, @MappingTarget Node node);
}