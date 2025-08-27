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
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.security.CustomUserPrincipal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/branch")
public class NodeController extends BaseController<NodeDTO, NodeService> {

	public NodeController(NodeService nodeService) {
		super(nodeService);
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
		model.addAttribute("breadcrumbs", service.generateBreadcrumbs(parentId));

		return "fragments/nodes/list";
	}

	@GetMapping({ "/nodelist", "/nodelist/{id}" })
	public String listNodesByParent(@PathVariable(value = "id", required = false) Long nodeId, Model model,
			Authentication authentication) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

		// Fallback to logged-in user's node if none provided
		if (nodeId == null) {
			nodeId = principal.getInstId();
		}

		if (principal.getInstId() != nodeId)
			model.addAttribute("isChild", true);
		else
			model.addAttribute("isChild", false);

		// Always resolve node once
		NodeDTO node = service.findById(nodeId);

		model.addAttribute("nodeName", node != null ? node.getNodeName() : principal.getNodeName());
		model.addAttribute("nodeType", node != null ? node.getNodeType() : principal.getNodeType());

		List<NodeDTO> nodeList = service.findByParentId(nodeId);

		model.addAttribute("nodeList", nodeList);
		model.addAttribute("parentId", nodeId);
		model.addAttribute("pageTitle", "Affiliations");
		model.addAttribute("target", "users_target");

		return "fragments/nodes/nodes";
	}

	@GetMapping("/node/view/{id}")
	public String viewNodeById(@PathVariable Long id, Model model) {
		NodeDTO node = service.findById(id);
		model.addAttribute("node", node);
		model.addAttribute("children", service.findChildren(id));
		model.addAttribute("pageTitle", "Affiliation Detail - " + node.getName());
		model.addAttribute("breadcrumbs", service.generateBreadcrumbs(id));
		return "fragments/nodes/node_detail";
	}

	@GetMapping("/node/add/{id}")
	public String showAddNodeForm(@PathVariable(value = "id") Long parentId, Model model,
			Authentication authentication) {
		
		model.addAttribute("pageTitle", "Add Affiliation");
		model.addAttribute("node", new NodeDTO());
		model.addAttribute("parentId", parentId);
		model.addAttribute("nodeType", service.getNextNodeType(parentId));
		return "fragments/nodes/add";
	}

	@PostMapping("/node/add/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addNode(@PathVariable(value = "id") Long parentId,@Valid @ModelAttribute NodeDTO nodeDTO, BindingResult result) {

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "branch_nodelist"+parentId);
		additionalData.put("target", "users_target");
		
		NodeDTO node = service.findById(parentId);
		nodeDTO.setNodeType(service.getNextNodeType(node.getParentId()));
		nodeDTO.setParentId(parentId);

		return handleRequest(result, () -> service.save(nodeDTO), "Affiliation added successfully", additionalData);
	}

	@GetMapping("/node/edit/{id}")
	public String editNode(@PathVariable Long id, Model model) {
		NodeDTO node = service.findById(id);
		model.addAttribute("nodeDTO", node);
		model.addAttribute("parentId", node.getParentId());
		model.addAttribute("nodeType", service.getNextNodeType(node.getParentId()));
		model.addAttribute("pageTitle", "Edit Affiliation - " + node.getName());
		return "fragments/nodes/edit";
	}

	@PostMapping("/node/edit/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateNode(@PathVariable Long id, @Valid @ModelAttribute NodeDTO nodeDTO,
			BindingResult result) {

		Map<String, Object> additionalData = new HashMap<>();
		NodeDTO node = service.findById(id);
		nodeDTO.setParentId(node.getParentId());
		additionalData.put("loadnext", "branch_nodelist/"+node.getParentId());
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.update(id, nodeDTO), "Affiliations updated successfully", additionalData);
	}

	@GetMapping("/node/tree")
	public String showTreeView(Model model, Authentication authentication) {

		CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
		model.addAttribute("parentId", principal.getInstId());

		model.addAttribute("userType", principal.getUserType());
		model.addAttribute("nodeType", principal.getNodeType());

		model.addAttribute("treeData", service.getSubTreeFromParent((Long) principal.getInstId()));
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
