package com.dms.kalari.mapper;

import com.dms.kalari.dto.NodeDTO;
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
public class NodeMapperImpl implements NodeMapper {

    @Override
    public NodeDTO toDTO(Node node) {
        if ( node == null ) {
            return null;
        }

        NodeDTO nodeDTO = new NodeDTO();

        nodeDTO.setParentId( nodeParentNodeId( node ) );
        nodeDTO.setParentName( nodeParentNodeName( node ) );
        if ( node.getNodeType() != null ) {
            nodeDTO.setNodeTypeLabel( node.getNodeType().name() );
        }
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

    @Override
    public Node toEntity(NodeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Node node = new Node();

        node.setActive( dto.getActive() );
        node.setDeleted( map( dto.getDeleted() ) );
        node.setTCreated( dto.getTCreated() );
        node.setTDeleted( dto.getTDeleted() );
        node.setTModified( dto.getTModified() );
        node.setUCreated( dto.getUCreated() );
        node.setUDeleted( dto.getUDeleted() );
        node.setUModified( dto.getUModified() );
        node.setChildren( nodeDTOListToNodeList( dto.getChildren() ) );
        node.setNodeId( dto.getNodeId() );
        node.setNodeName( dto.getNodeName() );
        node.setNodeStatus( dto.getNodeStatus() );
        node.setNodeType( dto.getNodeType() );

        node.setParent( mapParentIdToNode(dto.getParentId()) );

        return node;
    }

    @Override
    public void updateNodeFromDto(NodeDTO nodeDTO, Node node) {
        if ( nodeDTO == null ) {
            return;
        }

        if ( nodeDTO.getActive() != null ) {
            node.setActive( nodeDTO.getActive() );
        }
        if ( nodeDTO.getDeleted() != null ) {
            node.setDeleted( map( nodeDTO.getDeleted() ) );
        }
        if ( nodeDTO.getTCreated() != null ) {
            node.setTCreated( nodeDTO.getTCreated() );
        }
        if ( nodeDTO.getTDeleted() != null ) {
            node.setTDeleted( nodeDTO.getTDeleted() );
        }
        if ( nodeDTO.getTModified() != null ) {
            node.setTModified( nodeDTO.getTModified() );
        }
        if ( nodeDTO.getUCreated() != null ) {
            node.setUCreated( nodeDTO.getUCreated() );
        }
        if ( nodeDTO.getUDeleted() != null ) {
            node.setUDeleted( nodeDTO.getUDeleted() );
        }
        if ( nodeDTO.getUModified() != null ) {
            node.setUModified( nodeDTO.getUModified() );
        }
        if ( node.getChildren() != null ) {
            List<Node> list = nodeDTOListToNodeList( nodeDTO.getChildren() );
            if ( list != null ) {
                node.getChildren().clear();
                node.getChildren().addAll( list );
            }
        }
        else {
            List<Node> list = nodeDTOListToNodeList( nodeDTO.getChildren() );
            if ( list != null ) {
                node.setChildren( list );
            }
        }
        if ( nodeDTO.getNodeId() != null ) {
            node.setNodeId( nodeDTO.getNodeId() );
        }
        if ( nodeDTO.getNodeName() != null ) {
            node.setNodeName( nodeDTO.getNodeName() );
        }
        if ( nodeDTO.getNodeStatus() != null ) {
            node.setNodeStatus( nodeDTO.getNodeStatus() );
        }
        if ( nodeDTO.getNodeType() != null ) {
            node.setNodeType( nodeDTO.getNodeType() );
        }
    }

    private Long nodeParentNodeId(Node node) {
        if ( node == null ) {
            return null;
        }
        Node parent = node.getParent();
        if ( parent == null ) {
            return null;
        }
        Long nodeId = parent.getNodeId();
        if ( nodeId == null ) {
            return null;
        }
        return nodeId;
    }

    private String nodeParentNodeName(Node node) {
        if ( node == null ) {
            return null;
        }
        Node parent = node.getParent();
        if ( parent == null ) {
            return null;
        }
        String nodeName = parent.getNodeName();
        if ( nodeName == null ) {
            return null;
        }
        return nodeName;
    }

    protected List<NodeDTO> nodeListToNodeDTOList(List<Node> list) {
        if ( list == null ) {
            return null;
        }

        List<NodeDTO> list1 = new ArrayList<NodeDTO>( list.size() );
        for ( Node node : list ) {
            list1.add( toDTO( node ) );
        }

        return list1;
    }

    protected List<Node> nodeDTOListToNodeList(List<NodeDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Node> list1 = new ArrayList<Node>( list.size() );
        for ( NodeDTO nodeDTO : list ) {
            list1.add( toEntity( nodeDTO ) );
        }

        return list1;
    }
}
