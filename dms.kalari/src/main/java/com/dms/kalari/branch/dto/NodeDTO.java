package com.dms.kalari.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NodeDTO extends BaseDTO {
    private Long nodeId;

    @NotBlank(message = "Node name is required")
    @Size(max = 100, message = "Node name must be less than 100 characters")
    private String nodeName;  // This provides getName() via @Data annotation
    
    public String getName() {
        return this.nodeName;
    }
    
    private Boolean isActivePath = false;

    //@NotNull(message = "Node type is required")
    private Node.Type nodeType; // Reference the enum from Node entity

    @NotNull(message = "Status is required")
    private Short nodeStatus = 1;

    private Long parentId;
    private String parentName;
    private String nodeTypeLabel;
    //private List<NodeDTO> children;
}