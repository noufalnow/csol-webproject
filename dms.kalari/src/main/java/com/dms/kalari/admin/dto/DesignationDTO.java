package com.dms.kalari.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.dms.kalari.common.BaseDTO;

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
