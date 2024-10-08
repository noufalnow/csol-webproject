package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO extends BaseDTO {
    private Long bldId;
    private String bldName;
    private String bldNo;
    private String bldArea;
    private String bldBlockNo;
    private String bldPlotNo;
    private String bldWay;
    private String bldStreet;
    private String bldBlock;
}
