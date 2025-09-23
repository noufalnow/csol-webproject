package com.dms.kalari.branch.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.dto.NodeFlatDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.repository.NodeRepository;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.security.CustomUserPrincipal;
import com.dms.kalari.util.XorMaskHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/branch")
public class NodeController extends BaseController<NodeDTO, NodeService> {
	
	
	private final NodeRepository nodeRepository;

	public NodeController(NodeService nodeService,NodeRepository nodeRepository) {
		super(nodeService);
		this.nodeRepository = nodeRepository;
	}
	
	

	@GetMapping("/nodes")
	public String listNodes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(required = false) String search, @RequestParam(required = false) Long parentId, Model model,
			Authentication authentication) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		if (parentId == null) {
			parentId = principal.getInstId(); // fallback to logged-in user's node
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

		logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}, ParentId: {}", page,
				size, sortField, sortDir, search, parentId);

		Page<NodeDTO> nodePage = service.findAllPaginate(pageable, search, parentId);

		logInfo("Node Page - Total Elements: {}, Total Pages: {}", nodePage.getTotalElements(),
				nodePage.getTotalPages());

		setupPagination(model, nodePage, sortField, sortDir);

		model.addAttribute("search", search);
		model.addAttribute("parentId", parentId);
		model.addAttribute("pageTitle", " ");
		model.addAttribute("pageUrl", "/nodes/html");

		return "fragments/nodes/list";
	}

	@GetMapping({ "/nodelist", "/nodelist/{id}" })
	public String listNodesByParent(@PathVariable(value = "id", required = false) Long nodeId,
	                                Model model,
	                                Authentication authentication) {

	    CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

	    // Fallback to logged-in user's node if none provided
	    if (nodeId == null) {
	        nodeId = principal.getInstId();
	    } else {
	        nodeId = XorMaskHelper.unmask(nodeId);
	    }

	    model.addAttribute("isChild", !principal.getInstId().equals(nodeId));

	    // Always resolve node once
	    NodeDTO node = service.findNodeById(nodeId);

	    model.addAttribute("nodeName", node != null ? node.getNodeName() : principal.getNodeName());
	    model.addAttribute("nodeType", node != null ? node.getNodeType() : principal.getNodeType());

		List<NodeDTO> nodeList = service.findByParentId(nodeId);

		model.addAttribute("nodeList", nodeList);
		model.addAttribute("parentId", nodeId);

	    model.addAttribute("nodeList", nodeList);

	    model.addAttribute("parentId", XorMaskHelper.mask(nodeId));
	    model.addAttribute("pageTitle", "Affiliations");
	    model.addAttribute("target", "users_target");

	    return "fragments/nodes/nodes";
	}


	@GetMapping("/node/view/{id}")
	public String viewNodeById(@PathVariable Long id, Model model) {
		
		Long nodeId = XorMaskHelper.unmask(id);
		
		NodeDTO node = service.findById(nodeId);
		model.addAttribute("node", node);
		model.addAttribute("children", service.findChildren(nodeId));
		model.addAttribute("pageTitle", "Affiliation Detail - " + node.getName());
		return "fragments/nodes/view";
	}

	@GetMapping("/node/add/{id}")
	public String showAddNodeForm(@PathVariable(value = "id") Long mParentId, Model model,
			Authentication authentication) {
		
		Long parentId = XorMaskHelper.unmask(mParentId);
		
		model.addAttribute("pageTitle", "Add Affiliation");
		model.addAttribute("node", new NodeDTO());
		model.addAttribute("parentId", mParentId);
		//model.addAttribute("nodeType", service.getNextNodeType(parentId));
		return "fragments/nodes/add";
	}

	@PostMapping("/node/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addNode(@PathVariable(value = "id") Long mParentId,@Valid @ModelAttribute NodeDTO nodeDTO,
			BindingResult result,
			Authentication authentication
			) {

		Long parentId = XorMaskHelper.unmask(mParentId);
		
	    //Validate allowed node IDs
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
	    List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds( principal.getInstId());
	    	    
	    
	    if (!allowedNodeIds.contains(parentId)) {
	        throw new SecurityException("Invalid node submitted!");
	    }
		
		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "branch_tree");
		additionalData.put("target", "content");
		
		//NodeDTO node = service.findById(parentId);
		nodeDTO.setNodeType(service.getNextNodeType(parentId));
		nodeDTO.setParentId(parentId);

		return handleRequest(result, () -> service.save(nodeDTO), "Affiliation added successfully", additionalData);
	}

	@GetMapping("/node/edit/{mId}")
	public String editNode(@PathVariable Long mId, Model model) {
		
		Long id = XorMaskHelper.unmask(mId);
		
		NodeDTO node = service.findById(id);
		model.addAttribute("nodeDTO", node);
		model.addAttribute("nodeId", mId);
		model.addAttribute("nodeType", service.getNextNodeType(node.getParentId()));
		model.addAttribute("pageTitle", "Edit Affiliation - " + node.getName());
		return "fragments/nodes/edit";
	}

	@PostMapping("/node/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateNode(@PathVariable Long id, @Valid @ModelAttribute NodeDTO nodeDTO,BindingResult result,
			Authentication authentication
			) {
		
		Long nodeId = XorMaskHelper.unmask(id);
		
	    //Validate allowed node IDs
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
	    List<Long> allowedNodeIds = nodeRepository.findAllowedNodeIds( principal.getInstId());
	    
	    if (!allowedNodeIds.contains(nodeId)) {
	        throw new SecurityException("Invalid node submitted!");
	    }

		Map<String, Object> additionalData = new HashMap<>();
		NodeDTO node = service.findById(nodeId);
		nodeDTO.setParentId(node.getParentId());
		additionalData.put("loadnext", "branch_nodelist/"  + XorMaskHelper.mask(node.getParentId()));
		additionalData.put("target", "users_target");
		return handleRequest(result, () -> service.update(nodeId, nodeDTO), "Affiliations updated successfully", additionalData);
	}

	@GetMapping("/node/tree")
	public String showTreeView(Model model, Authentication authentication) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		model.addAttribute("parentId", principal.getInstId());

		model.addAttribute("userType", principal.getUserType());
		model.addAttribute("nodeType", principal.getNodeType());

		Map<String, Object> treeData = service.getSubTreeFromParent((Long) principal.getInstId());

		model.addAttribute("groupedTree", treeData.get("grouped"));
		model.addAttribute("rootNode", treeData.get("rootNode"));
		model.addAttribute("parentId", treeData.get("parentId")); // 👈 pass to view

	    
		model.addAttribute("pageTitle", "Organizational Structure");
		return "fragments/nodes/node_tree";
	}

	/*
	 * @GetMapping("/html/tree") public String showTreeView(Model model) {
	 * List<Map<String,Object>> fullTree = service.getFullTree();
	 * 
	 * // build the HTML string once here String treeHtml = buildTreeHtml(fullTree);
	 * 
	 * model.addAttribute("treeHtml", treeHtml); model.addAttribute("pageTitle",
	 * "Node Tree View"); return "fragments/node_tree"; }
	 * 
	 * 
	 * /** Takes your List<Map<String,Object>> tree and returns a single String of
	 * nested <ul>/<li> markup.
	 */
	/*
	 * public String buildTreeHtml(List<Map<String,Object>> nodes) { StringBuilder
	 * sb = new StringBuilder(); sb.append("<ul>"); for (Map<String,Object> node :
	 * nodes) { // get the display name String name =
	 * Objects.toString(node.get("nodeName"), ""); sb.append("<li>").append(name);
	 * 
	 * // if there are children, recurse Object kids = node.get("children"); if
	 * (kids instanceof List<?> children && !children.isEmpty()) { //noinspection
	 * unchecked sb.append(buildTreeHtml((List<Map<String,Object>>)children)); }
	 * 
	 * sb.append("</li>"); } sb.append("</ul>"); return sb.toString(); }
	 * 
	 */

}
