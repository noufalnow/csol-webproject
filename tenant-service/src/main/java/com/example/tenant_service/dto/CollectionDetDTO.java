package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;
import com.example.tenant_service.entity.MisPropertyPayOption;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CollectionDetDTO {
    private Long cdetId;
    private Long cdetCollId;
    private Long cdetPoptId;
    private BigDecimal cdetAmtToPay;
    private BigDecimal cdetAmtPaid;
    
    private MisPropertyPayOption misPropertyPayoption;
}
