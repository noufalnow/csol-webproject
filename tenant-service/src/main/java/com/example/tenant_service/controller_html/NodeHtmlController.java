package com.example.tenant_service.controller_html;

import com.example.tenant_service.service.NodeService;
import com.example.tenant_service.dto.NodeDTO;
import jakarta.validation.Valid;
import com.example.tenant_service.common.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/nodes")
public class NodeHtmlController extends BaseController<NodeDTO, NodeService> {

    public NodeHtmlController(NodeService nodeService) {
        super(nodeService);
    }

    @GetMapping("/html")
    public String listNodes(HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long parentId,
            Model model) {
    	
    	
    	Object parentIdAttr = session.getAttribute("ParentId");
    	parentId = parentIdAttr != null ? Long.parseLong(parentIdAttr.toString()) : null;

    	logInfo("Parent ID: {}", parentId);


    	
    	//Long effectiveParentId = (parentId != null) ? parentId : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));

        logInfo("Request Parameters - Page: {}, Size: {}, SortField: {}, SortDir: {}, Search: {}, ParentId: {}",
                page, size, sortField, sortDir, search, parentId);

        Page<NodeDTO> nodePage = service.findAllPaginate(pageable, search, parentId);

        logInfo("Node Page - Total Elements: {}, Total Pages: {}", nodePage.getTotalElements(), nodePage.getTotalPages());

        setupPagination(model, nodePage, sortField, sortDir);

        model.addAttribute("search", search);
        model.addAttribute("parentId", parentId);
        model.addAttribute("pageTitle", " ");
        model.addAttribute("pageUrl", "/nodes/html");
        model.addAttribute("breadcrumbs", service.generateBreadcrumbs(parentId));

        return "fragments/node_list";
    }

    @GetMapping("/html/{id}")
    public String viewNodeById(@PathVariable Long id, Model model) {
        NodeDTO node = service.findById(id);
        model.addAttribute("node", node);
        model.addAttribute("children", service.findChildren(id));
        model.addAttribute("pageTitle", "Node Detail - " + node.getName());
        model.addAttribute("breadcrumbs", service.generateBreadcrumbs(id));
        return "fragments/node_detail";
    }

    @GetMapping("/html/add")
    public String showAddNodeForm(
            @RequestParam(required = false) Long parentId,
            Model model) {
        model.addAttribute("pageTitle", "Add Node - My Application");
        model.addAttribute("node", new NodeDTO());
        model.addAttribute("parentId", parentId);
        //model.addAttribute("availableTypes", service.getAvailableNodeTypes(parentId));
        model.addAttribute("nodeType", service.getNextNodeType(parentId));
        return "fragments/add_node";
    }

    @PostMapping("/html/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addNode(
            @Valid @ModelAttribute NodeDTO nodeDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/nodes/html");
        
        return handleRequest(result, () -> service.save(nodeDTO),
                "Node added successfully", additionalData);
    }

    @GetMapping("/html/edit/{id}")
    public String editNode(@PathVariable Long id, Model model,HttpServletRequest request) {
        NodeDTO node = service.findById(id);
        model.addAttribute("nodeDTO", node);
        
 	    HttpSession session = request.getSession(false);
 	    Long parentId = (Long) session.getAttribute("ParentId");
 	    model.addAttribute("parentId", parentId);
        
        model.addAttribute("nodeType", service.getNextNodeType(parentId));
        //model.addAttribute("availableTypes", service.getAvailableNodeTypes(node.getParentId()));
        model.addAttribute("pageTitle", "Edit Node - " + node.getName());
        return "fragments/edit_node";
    }

    @PostMapping("/html/update/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateNode(
            @PathVariable Long id,
            @Valid @ModelAttribute NodeDTO nodeDTO,
            BindingResult result) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loadnext", "/nodes/html");

        return handleRequest(result, () -> service.update(id, nodeDTO),
                "Node updated successfully", additionalData);
    }


    
	@GetMapping("/html/tree")
	public String showTreeView(Model model, HttpServletRequest request) {
		
 	    HttpSession session = request.getSession(false);
 	    model.addAttribute("parentId", (Long) session.getAttribute("NODE_ID"));
 	    
 	    model.addAttribute("userType",session.getAttribute("USER_TYPE").toString().trim());
 	    model.addAttribute("nodeType",session.getAttribute("NODE_TYPE").toString().trim());
 	    
		model.addAttribute("treeData", service.getFullTreeWithActivePath((Long) session.getAttribute("NODE_ID")));
		model.addAttribute("pageTitle", "Organizational Structure");
		return "fragments/node_tree";
	}
	
	
    /*@GetMapping("/html/tree")
    public String showTreeView(Model model) {
        List<Map<String,Object>> fullTree = service.getFullTree();
        
        // build the HTML string once here
        String treeHtml = buildTreeHtml(fullTree);
        
        model.addAttribute("treeHtml", treeHtml);
        model.addAttribute("pageTitle", "Node Tree View");
        return "fragments/node_tree"; 
    }
	
 
    /**
     * Takes your List<Map<String,Object>> tree and returns a
     * single String of nested <ul>/<li> markup.
     */
    /*public String buildTreeHtml(List<Map<String,Object>> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Map<String,Object> node : nodes) {
            // get the display name
            String name = Objects.toString(node.get("nodeName"), "");
            sb.append("<li>").append(name);
            
            // if there are children, recurse
            Object kids = node.get("children");
            if (kids instanceof List<?> children && !children.isEmpty()) {
                //noinspection unchecked
                sb.append(buildTreeHtml((List<Map<String,Object>>)children));
            }
            
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    */
    
    
    
}
