package com.dms.kalari.branch.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.branch.dto.NodeDTO;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.common.BaseController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/branch")
public class NodeController extends BaseController<NodeDTO, NodeService> {

	public NodeController(NodeService nodeService) {
		super(nodeService);
	}

	@GetMapping("/nodes")
	public String listNodes(HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortField,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestParam(required = false) String search,
			@RequestParam(required = false) Long parentId, Model model) {

		Object parentIdAttr = session.getAttribute("ParentId");
		parentId = parentIdAttr != null ? Long.parseLong(parentIdAttr.toString()) : null;

		logInfo("Parent ID: {}", parentId);

		// Long effectiveParentId = (parentId != null) ? parentId : null;

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

		return "fragments/nodes/node_list";
	}

	@GetMapping({ "/nodelist", "/nodelist/{id}" }) // Supports both patterns
	public String listNodesByParent(@PathVariable(value = "id", required = false) Long parentId, HttpSession session,
			Model model) {

		// HttpSession session = request.getSession();
		if (parentId != null) {
			session.setAttribute("ParentId", parentId); // for data entry
		} else {
			parentId = (Long) session.getAttribute("ParentId");
		}
		if (parentId == null) {
			parentId = (Long) session.getAttribute("NODE_ID");
		}

		List<NodeDTO> nodeList = service.findByParentId(parentId);

		NodeDTO node = service.findById(parentId);

		Node.Type nodeType = (Node.Type) node.getNodeType();
		Node.Type userNodeType = (Node.Type) session.getAttribute("NODE_TYPE");

		//logInfo("My Node ID: {}", session.getAttribute("NODE_ID"));
		//logInfo("Selected Node ID: {}", node.getNodeId());
				

		if ((nodeType.getLevel() >= userNodeType.getLevel()) &&  (userNodeType != Node.Type.KALARI) && (nodeType != Node.Type.KALARI)) {
			model.addAttribute("allowAddNode", true);
		}

		model.addAttribute("nodeList", nodeList);
		model.addAttribute("parentId", parentId);

		model.addAttribute("pageTitle", "Branches");
		model.addAttribute("target", "users_target");

		model.addAttribute("target", "users_target");
		return "fragments/nodes/node_list";
	}

	@GetMapping("/node/view/{id}")
	public String viewNodeById(@PathVariable Long id, Model model) {
		NodeDTO node = service.findById(id);
		model.addAttribute("node", node);
		model.addAttribute("children", service.findChildren(id));
		model.addAttribute("pageTitle", "Branch Detail - " + node.getName());
		model.addAttribute("breadcrumbs", service.generateBreadcrumbs(id));
		return "fragments/nodes/node_detail";
	}

	@GetMapping("/node/add")
	public String showAddNodeForm(@RequestParam(required = false) Long parentId, Model model) {
		model.addAttribute("pageTitle", "Add Branch");
		model.addAttribute("node", new NodeDTO());
		model.addAttribute("parentId", parentId);
		// model.addAttribute("availableTypes",
		// service.getAvailableNodeTypes(parentId));
		model.addAttribute("nodeType", service.getNextNodeType(parentId));
		return "fragments/nodes/add_node";
	}

	@PostMapping("/node/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addNode(@Valid @ModelAttribute NodeDTO nodeDTO, BindingResult result,
			HttpSession session) {

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "nodes/html/nodelist");
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.save(nodeDTO), "Node added successfully", additionalData);
	}

	@GetMapping("/node/edit/{id}")
	public String editNode(@PathVariable Long id, Model model, HttpServletRequest request) {
		NodeDTO node = service.findById(id);
		model.addAttribute("nodeDTO", node);

		HttpSession session = request.getSession(false);
		Long parentId = (Long) session.getAttribute("ParentId");
		model.addAttribute("parentId", parentId);

		model.addAttribute("nodeType", service.getNextNodeType(parentId));
		// model.addAttribute("availableTypes",
		// service.getAvailableNodeTypes(node.getParentId()));
		model.addAttribute("pageTitle", "Edit Branch - " + node.getName());
		return "fragments/nodes/edit_node";
	}

	@PostMapping("/node/update/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateNode(@PathVariable Long id, @Valid @ModelAttribute NodeDTO nodeDTO,
			BindingResult result) {

		Map<String, Object> additionalData = new HashMap<>();
		additionalData.put("loadnext", "nodes/html/nodelist");
		additionalData.put("target", "users_target");

		return handleRequest(result, () -> service.update(id, nodeDTO), "Node updated successfully", additionalData);
	}

	@GetMapping("/node/tree")
	public String showTreeView(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		model.addAttribute("parentId", (Long) session.getAttribute("NODE_ID"));

		model.addAttribute("userType", session.getAttribute("USER_TYPE").toString().trim());
		model.addAttribute("nodeType", session.getAttribute("NODE_TYPE").toString().trim());

		model.addAttribute("treeData", service.getSubTreeFromParent((Long) session.getAttribute("NODE_ID")));
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
