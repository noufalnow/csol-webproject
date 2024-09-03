package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO extends BaseDTO {
    private Long propId;
    private String propNo;
    private String propName;
    @NotBlank(message = "File number is mandatory")
    private String propFileno;
    private Short propBuilding;
    private Long propResponsible;
    private String propRemarks;
    private Short propCat;
    private Short propType;
    private Short propLevel;
    private String propElecMeter;
    private String propWater;
    private Short propBuildingType;
    private Long propStatus;
    private String propElecAccount;
    private String propElecRecharge;
    private Long propAccount;
}
