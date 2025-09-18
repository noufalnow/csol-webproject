package com.dms.kalari.admin.controller;

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

import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.service.MisDesignationService;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseController;
import com.dms.kalari.util.XorMaskHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/designations")
public class DesignationController extends BaseController<DesignationDTO, MisDesignationService> {

    public DesignationController(MisDesignationService service) {
        super(service);
    }

    /** List + mask IDs */
    @GetMapping("/list")
    public String listDesignations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desigId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Node.Type level,   // <-- enum directly
            @RequestParam(required = false) Short type,        // <-- Short directly
            Model model) {
    	
    	
    	if (sortField == null || sortField.isEmpty() || "undefined".equals(sortField)) {
    	    sortField = "desigId";
    	}

        Pageable pageable = PageRequest.of(page, size,
            Sort.by(Sort.Direction.fromString(sortDir), sortField));

        Page<DesignationDTO> desigPage =
            service.findAllPaginate(pageable, code, name, level, type);

        setupPagination(model, desigPage, sortField, sortDir);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("items", desigPage.getContent());
        model.addAttribute("totalCount", desigPage.getTotalElements());

        
        Map<String,String> paramx = new HashMap<>();
        paramx.put("code",  code);
        paramx.put("name",  name);
        paramx.put("level", level  != null ? level.name()    : "");
        paramx.put("type",  type   != null ? type.toString() : "");
        model.addAttribute("paramx", paramx);
        
        model.addAttribute("pageTitle", "Designation List");
        model.addAttribute("pageUrl", "/designations");

        return "fragments/admin/designations/list";
    }




    /** Add form */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("designation", new DesignationDTO());
        model.addAttribute("pageTitle", "Add Designation");
        return "fragments/admin/designations/add";
    }

    /** Save new */
    @PostMapping("/add")
    @ResponseBody
    
    public ResponseEntity<Map<String,Object>> add(@Valid @ModelAttribute DesignationDTO dto,
                                                  BindingResult result,
                                                  @RequestParam(name = "pageParams", required = false) String pageParams) {

        Map<String,Object> extra = new HashMap<>();
        extra.put("loadnext", "designations?" + (pageParams != null ? pageParams : ""));

        return handleRequest(result,
                             () -> service.save(dto),
                             "Designation added successfully",
                             extra);
    }


    /** Edit form – unmask ID before service call */
    @GetMapping("/edit/{maskedId}")
    public String editForm(@PathVariable Long maskedId, Model model) {
        Long realId = XorMaskHelper.unmask(maskedId);
        DesignationDTO dto = service.findById(realId);
        dto.setDesigId(maskedId);
        model.addAttribute("designation", dto);
        model.addAttribute("pageTitle", "Edit Designation");
        return "fragments/admin/designations/edit";
    }

    /** Update – unmask only in controller */
    @PostMapping("/edit/{maskedId}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> update(@PathVariable Long maskedId,
                                                     @Valid @ModelAttribute DesignationDTO dto,
                                                     BindingResult result,@RequestParam(name = "pageParams", required = false) String pageParams) {

        Long realId = XorMaskHelper.unmask(maskedId);
        Map<String,Object> extra = new HashMap<>();
        extra.put("loadnext", "designations?" + (pageParams != null ? pageParams : ""));
        return handleRequest(result,
                () -> service.update(realId, dto),
                "Designation updated successfully",
                extra);
    }

}

