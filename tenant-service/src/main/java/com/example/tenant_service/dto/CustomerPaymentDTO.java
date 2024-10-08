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
public class CustomerPaymentDTO {
    private String tntFullName;
    private String tntCompName;
    private String tntPhone;
    private BigDecimal dueAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
}
