package com.dms.kalari.branch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.repository.CoreUserRepository;
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
	private final CoreUserRepository coreUserRepository;

	@Autowired
	public NodeService(NodeRepository nodeRepository, NodeMapper nodeMapper, CoreFileRepository coreFileRepository,CoreUserRepository coreUserRepository) {
		this.nodeRepository = nodeRepository;
		this.nodeMapper = nodeMapper;
		this.coreFileRepository = coreFileRepository;
		this.coreUserRepository = coreUserRepository;
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
	
	
	
	public List<Long> findAllowedNodeIds(Long parentId) {
		return nodeRepository.findAllowedNodeIds(parentId);
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
			return List.of(Node.Type.NATIONAL);
		case NATIONAL:
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
			return Node.Type.NATIONAL;
		case NATIONAL:
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
            file.setFileSrc("nodes");
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
    
    
    public CoreFile uploadNodeFile(Long nodeId, MultipartFile file) {
        Node node = nodeRepository.findByIdAndNotDeleted(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Node", nodeId));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file provided");
        }

        try {
            // Save file metadata
            CoreFile newFile = new CoreFile();
            newFile.setFileSrc("nodes_files");
            newFile.setFileRefId(nodeId);
            newFile.setFileActualName(file.getOriginalFilename());
            newFile.setFileExten(getExtension(file.getOriginalFilename()));
            newFile.setFileSize(file.getSize());

            // Save file to disk
            Path uploadPath = Paths.get("uploads/nodes/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            newFile.setFilePath(filePath.toString());

            // Save metadata in DB
            CoreFile savedFile = coreFileRepository.save(newFile);

            return savedFile;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
    
    
    public List<Map<String, Object>> getBranchOfficials(Long nodeId) {

    	return coreUserRepository
    	        .findBranchOfficials(nodeId, UserType.OFFICIAL)
    	        .stream()
    	        .map(u -> {
    	            Map<String, Object> m = new HashMap<>();

    	            m.put("id", u.getUserId());
    	            m.put("name", u.getUserFname() + " " + u.getUserLname());
    	            m.put("designation", u.getDesignation().getDesigName());
    	            m.put("phone", u.getMobileNumber());
    	            m.put("email", u.getUserEmail());

    	            // ✅ photo (real file mapping)
    	            if (u.getPhotoFile() != null) {
    	                coreFileRepository.findById(u.getPhotoFile())
    	                        .ifPresent(file -> m.put("photo", "https://app.indiankalaripayattufederation.com/image_public/" + file.getFileId()));
    	            } else {
    	                m.put("photo", null);
    	            }

    	            return m;
    	        })
    	        .toList();
    }
    
    public Map<String, Object> getFullBranchDetails(String code, UUID id) {

        Node branch = resolveNode(code, id);

        return buildBranchResponse(branch);
    }
    
    private Node resolveNode(String code, UUID id) {

        if (code != null) {
            return nodeRepository.findByBranchCode(code)
                    .orElseThrow(() -> new RuntimeException("Branch not found (code)"));
        }

        if (id != null) {
            return nodeRepository.findByBranchRandomId(id)
                    .orElseThrow(() -> new RuntimeException("Branch not found (id)"));
        }

        throw new IllegalArgumentException("Either code or id must be provided");
    }
    
    
    public Map<String, Object> buildBranchResponse(Node branch) {


        Map<String, Object> response = new HashMap<>();

        // basic
        Map<String, Object> branchMap = new HashMap<>();

        branchMap.put("id", branch.getBranchRandomId());
        branchMap.put("name", branch.getNodeName());
        branchMap.put("type", branch.getNodeType());
        branchMap.put("code", branch.getBranchCode());

        // ✅ Address (group it, don’t scatter)
        Map<String, Object> address = new HashMap<>();
        address.put("line1", branch.getAddressLine1());
        address.put("line2", branch.getAddressLine2());
        address.put("line3", branch.getAddressLine3());
        address.put("state", branch.getAddressState());
        address.put("pin", branch.getAddressPin());

        branchMap.put("address", address);
        
        
        Map<String, Object> about = new HashMap<>();
        
        
        about.put("branch_activity", branch.getBranchActivity());
        about.put("branch_vision", branch.getBranchVision());
        about.put("branch_history", branch.getBranchHistory());
        
        branchMap.put("anout", about);

        // ✅ Optional extras (safe)
        branchMap.put("registerNumber", branch.getRegisterNumber());
        branchMap.put("photo", "https://app.indiankalaripayattufederation.com/image_public/" +branch.getPhotoFile());

        response.put("branch", branchMap);

        // officials
        response.put("officials",
                this.getBranchOfficials(
                        branch.getNodeId()));
        
        


        
        List<Map<String, Object>> children = nodeRepository
                .findByParentId(branch.getNodeId())
                .stream()
                .map(node -> {
                    Map<String, Object> c = new HashMap<>();
                    c.put("id", branch.getBranchRandomId());
                    c.put("name", node.getNodeName());
                    c.put("type", node.getNodeType());
                    c.put("code", node.getBranchCode());

                    // optional (only if useful)
                    c.put("state", node.getAddressState());
                    c.put("photo", node.getPhotoFile());

                    return c;
                })
                .toList();

        response.put("children", children);


        return response;
    }







}