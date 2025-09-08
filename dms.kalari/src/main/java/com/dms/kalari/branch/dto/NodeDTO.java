package com.dms.kalari.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import org.springframework.web.multipart.MultipartFile;

import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.util.ValidFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NodeDTO extends BaseDTO {
    private Long nodeId;

    @NotBlank(message = "Node name is required")
    @Size(max = 100, message = "Node name must be less than 100 characters")
    private String nodeName;

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

    // ✅ Address fields (mandatory)
    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;

    @NotBlank(message = "Address Line 2 is required")
    private String addressLine2;

    @NotBlank(message = "Address Line 3 is required")
    private String addressLine3;

    @NotBlank(message = "State is required")
    private String addressState;

    @NotBlank(message = "PIN code is required")
    @Size(min = 6, max = 6, message = "PIN code must be exactly 6 characters")
    private String addressPin;

    // ✅ Register Number
    @NotBlank(message = "Register number is required")
    @Size(max = 15, message = "Register number cannot exceed 15 characters")
    private String registerNumber;
    
    
    @ValidFile(maxSize = 524288, allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile photoFileId;
    
    private String filePath;  
    
    private Long photoFile;
}
