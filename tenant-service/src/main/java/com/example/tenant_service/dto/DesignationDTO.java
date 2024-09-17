package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationDTO extends BaseDTO {
    
    private Long desigId;
    private String desigCode;
    private String desigName;
    private Long desigLevel;
    private Short desigType;
}
