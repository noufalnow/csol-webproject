package com.dms.kalari.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.dms.kalari.common.BaseDTO;

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
