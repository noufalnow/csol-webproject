package com.dms.kalari.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseDTO;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationDTO extends BaseDTO {
    
    private Long desigId;
    private String desigCode;
    private String desigName;
    private Node.Type desigLevel;
    private Short desigType;
    
    
    public DesignationDTO(Long desigId, String desigName) {
        this.desigId = desigId;
        this.desigName = desigName;
    }
}
