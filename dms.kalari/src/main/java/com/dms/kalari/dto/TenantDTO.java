package com.dms.kalari.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.dms.kalari.common.BaseDTO;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO extends BaseDTO {
    private Long tntId;
    private String tntFullName;
    private String tntCompName;
    private String tntPhone;
    private String tntTele;
    private String tntIdNo;
    private String tntCrno;
    private Short tntExpat;
    private Short tntAgrType;
    private Long tntDocId;
}
