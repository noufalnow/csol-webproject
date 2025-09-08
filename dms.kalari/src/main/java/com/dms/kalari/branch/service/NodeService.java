package com.dms.kalari.branch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.dto.NodeFlatDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.mapper.NodeMapper;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.common.BaseService;
import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.core.repository.CoreFileRepository;
import com.dms.kalari.exception.ResourceNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NodeService implements BaseService<NodeDTO> {

	private final NodeRepository nodeRepository;
	private final NodeMapper nodeMapper;
	private final CoreFileRepository coreFileRepository;

	@Autowired
	public NodeService(NodeRepository nodeRepository, NodeMapper nodeMapper, CoreFileRepository coreFileRepository) {
		this.nodeRepository = nodeRepository;
		this.nodeMapper = nodeMapper;
		this.coreFileRepository = coreFileRepository;
	}

	@Override
	public NodeDTO save(NodeDTO nodeDTO) {
		validateNodeType(nodeDTO);
		Node node = nodeMapper.toEntity(nodeDTO);
		
		
        // 1. Save user first (to generate userId)
		Node savedNode = nodeRepository.save(node);

        // 2. Handle photo upload if provided
		savedNode = handleFileUpload(nodeDTO.getPhotoFileId(), savedNode);

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

	    updatedNode = handleFileUpload(nodeDTO.getPhotoFileId(), updatedNode);

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
    
    
    public List<NodeDTO> findByParentId(Long parentId) {
        return nodeRepository.findByParentId(parentId).stream().map(node -> nodeMapper.toDTO(node))
    			.collect(Collectors.toList());
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
	
	
	/************************ ONLY SUB TREE ---------------------------------
	 * @return */
	
	
	
	public Map<String, Object> getSubTreeFromParent(Long parentId) {
	    List<NodeFlatDTO> flatList = nodeRepository.findSubTreeFlat(parentId);

	    Map<Long, List<NodeFlatDTO>> grouped = new HashMap<>();
	    NodeFlatDTO rootNode = null;

	    for (NodeFlatDTO node : flatList) {
	        Long nodeId = node.getNodeId();
	        Long parentIdValue = node.getParentId();

	        grouped.computeIfAbsent(parentIdValue, k -> new ArrayList<>()).add(node);

	        if (node.getLvl() == 0 || nodeId.equals(parentId)) {
	            rootNode = node;
	        }
	    }

	    Map<String, Object> result = new HashMap<>();
	    result.put("grouped", grouped);
	    result.put("rootNode", rootNode);
	    result.put("parentId", parentId);

	    return result;
	}
	
	
    /**
     * Common method to handle file upload and metadata persistence.
     */
    private Node handleFileUpload(MultipartFile photoFile, Node node) {
        if (photoFile == null || photoFile.isEmpty()) {
            return node;
        }

        try {
            // Create file metadata
            CoreFile file = new CoreFile();
            file.setFileSrc("users");
            file.setFileRefId(node.getNodeId());
            file.setFileActualName(photoFile.getOriginalFilename());
            file.setFileExten(getExtension(photoFile.getOriginalFilename()));
            file.setFileSize(photoFile.getSize());

            // Save binary file to disk
            Path uploadPath = Paths.get("uploads/nodes/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photoFile.getInputStream(), filePath);

            // Store file path if needed
            file.setFilePath(filePath.toString());

            // Save file metadata
            CoreFile savedFile = coreFileRepository.save(file);

            // Link fileId to user
            node.setPhotoFile(savedFile.getFileId());
            node = nodeRepository.save(node);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        return node;
    }


    private String getExtension(String fileName) {
        return fileName != null && fileName.contains(".") 
               ? fileName.substring(fileName.lastIndexOf(".") + 1) 
               : null;
    }






}