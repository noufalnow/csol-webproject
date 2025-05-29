package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.mapper.NodeMapper;
import com.example.tenant_service.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NodeService implements BaseService<NodeDTO> {

	private final NodeRepository nodeRepository;
	private final NodeMapper nodeMapper;

	@Autowired
	public NodeService(NodeRepository nodeRepository, NodeMapper nodeMapper) {
		this.nodeRepository = nodeRepository;
		this.nodeMapper = nodeMapper;
	}

	@Override
	public NodeDTO save(NodeDTO nodeDTO) {
		validateNodeType(nodeDTO);
		Node node = nodeMapper.toEntity(nodeDTO);
		Node savedNode = nodeRepository.save(node);
		return nodeMapper.toDTO(savedNode);
	}

	@Override
	public NodeDTO update(Long nodeId, NodeDTO nodeDTO) {
		Node existingNode = nodeRepository.findByIdAndNotDeleted(nodeId)
				.orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));

		validateNodeType(nodeDTO);
		nodeMapper.updateNodeFromDto(nodeDTO, existingNode);
		existingNode.setTModified(LocalDateTime.now());

		Node updatedNode = nodeRepository.save(existingNode);
		return nodeMapper.toDTO(updatedNode);
	}

	@Override
	public List<NodeDTO> findAll() {
		return nodeRepository.findAllNotDeleted().stream().map(node -> nodeMapper.toDTO(node))
				.collect(Collectors.toList());
	}

	@Override
	public Page<NodeDTO> findAllPaginate(Pageable pageable, String search) {
		return nodeRepository.findAllNotDeleted(search, pageable).map(node -> nodeMapper.toDTO(node));
	}

	public Page<NodeDTO> findAllPaginate(Pageable pageable, String search, Long parentId) {
		if (parentId != null) {
			List<NodeDTO> content = nodeRepository.findByParentId(parentId).stream().map(node -> nodeMapper.toDTO(node))
					.collect(Collectors.toList());

			return PageableExecutionUtils.getPage(content, pageable, () -> nodeRepository.countByParentId(parentId));
		} else {
			List<NodeDTO> content = nodeRepository.findRootNodes().stream().map(node -> nodeMapper.toDTO(node))
					.collect(Collectors.toList());

			return PageableExecutionUtils.getPage(content, pageable, () -> nodeRepository.countRootNodes());
		}
	}

    @Override
    @Transactional // Keep this for write operations
    public NodeDTO findById(Long nodeId) {
        return nodeRepository.findByIdWithChildren(nodeId)
                .map(nodeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));
    }
    
    
    
    public NodeDTO findNodeById(Long nodeId) {
        return nodeRepository.findByIdAndNotDeleted(nodeId)
                .map(nodeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));
    }

	@Override
	public void softDeleteById(Long nodeId) {
		Node node = nodeRepository.findByIdAndNotDeleted(nodeId)
				.orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));
		node.setDeleted(true);
		node.setTDeleted(LocalDateTime.now());
		nodeRepository.save(node);
	}

	public boolean toggleNodeStatus(Long nodeId, Short nodeStatus) {
		Node node = nodeRepository.findById(nodeId).orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));
		node.setNodeStatus(nodeStatus);
		nodeRepository.save(node);
		return node.getNodeStatus() == 1;
	}

	private void validateNodeType(NodeDTO nodeDTO) {
		if (nodeDTO.getNodeType() == Node.Type.ROOT && nodeDTO.getParentId() != null) {
			throw new IllegalArgumentException("Root node cannot have a parent");
		}

		if (nodeDTO.getNodeType() != Node.Type.ROOT && nodeDTO.getParentId() == null) {
			throw new IllegalArgumentException("Non-root nodes must have a parent");
		}

		if (nodeDTO.getNodeType() == Node.Type.ROOT && nodeRepository.existsByNodeTypeAndDeletedFalse(Node.Type.ROOT)) {
			throw new IllegalArgumentException("Root node already exists");
		}
	}

	public List<NodeDTO> findChildren(Long parentId) {
		return nodeRepository.findByParentId(parentId).stream().map(nodeMapper::toDTO).collect(Collectors.toList());
	}

	public List<NodeDTO> generateBreadcrumbs(Long nodeId) {
		List<NodeDTO> breadcrumbs = new ArrayList<>();
		Node current = nodeRepository.findById(nodeId).orElse(null);
		while (current != null) {
			breadcrumbs.add(0, nodeMapper.toDTO(current));
			current = current.getParent();
		}
		return breadcrumbs;
	}

	public List<Node.Type> getAvailableNodeTypes(Long parentId) {
		if (parentId == null) {
			return List.of(Node.Type.ROOT);
		}
		Node parent = nodeRepository.findById(parentId).orElseThrow();
		switch (parent.getNodeType()) {
		case ROOT:
			return List.of(Node.Type.COUNTRY);
		case COUNTRY:
			return List.of(Node.Type.STATE);
		case STATE:
			return List.of(Node.Type.DISTRICT);
		case DISTRICT:
			return List.of(Node.Type.KALARI);
		default:
			return List.of();
		}
	}

	public Node.Type getNextNodeType(Long parentId) {
		if (parentId == null) {
			return Node.Type.ROOT;
		}
		Node parent = nodeRepository.findById(parentId).orElseThrow();
		switch (parent.getNodeType()) {
		case ROOT:
			return Node.Type.COUNTRY;
		case COUNTRY:
			return Node.Type.STATE;
		case STATE:
			return Node.Type.DISTRICT;
		case DISTRICT:
			return Node.Type.KALARI;
		default:
			throw new IllegalStateException("Unsupported node type: " + parent.getNodeType());
		}
	}

	/*
	 * public List<Map<String,Object>> getFullTree() { List<Node> roots =
	 * nodeRepository.findRootNodes(); return roots.stream()
	 * .map(this::buildNodeMap) .collect(Collectors.toList()); }
	 * 
	 * private Map<String, Object> buildNodeMap(Node node) { Map<String, Object> m =
	 * new HashMap<>(); // include nodeId m.put("nodeId", node.getNodeId());
	 * m.put("nodeName", node.getNodeName()); m.put("nodeType", node.getNodeType());
	 * if (!node.getChildren().isEmpty()) { List<Map<String, Object>> kids =
	 * node.getChildren().stream() .map(this::buildNodeMap)
	 * .collect(Collectors.toList()); m.put("children", kids); } return m; }
	 */

	public List<Map<String, Object>> getFullTreeWithActivePath(Long parentId) {

		List<Node> roots = nodeRepository.findRootNodes();
		Set<Long> activePath = parentId != null ? findActivePath(roots, parentId) : Collections.emptySet();

		return roots.stream().map(node -> buildNodeMapWithActivePath(node, activePath)).collect(Collectors.toList());
	}

	private Set<Long> findActivePath(List<Node> nodes, Long targetId) {
		Set<Long> path = new HashSet<>();
		findPath(nodes, targetId, path);
		return path;
	}

	private boolean findPath(List<Node> nodes, Long targetId, Set<Long> path) {
		for (Node node : nodes) {
			if (node.getNodeId().equals(targetId)) {
				path.add(node.getNodeId());
				return true;
			}
			if (!node.getChildren().isEmpty() && findPath(node.getChildren(), targetId, path)) {
				path.add(node.getNodeId());
				return true;
			}
		}
		return false;
	}

	private Map<String, Object> buildNodeMapWithActivePath(Node node, Set<Long> activePath) {
	    Map<String, Object> m = new HashMap<>();
	    m.put("nodeId", node.getNodeId());
	    m.put("nodeName", node.getNodeName());
	    m.put("nodeType", node.getNodeType());
	    m.put("isActivePath", activePath.contains(node.getNodeId()));
	    
	    // Always include children, even if empty
	    List<Map<String, Object>> kids = node.getChildren().stream()
	        .map(child -> buildNodeMapWithActivePath(child, activePath))
	        .collect(Collectors.toList());
	    m.put("children", kids);
	    
	    return m;
	}

}