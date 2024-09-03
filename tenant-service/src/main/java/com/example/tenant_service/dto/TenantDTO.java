package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
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
