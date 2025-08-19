package com.dms.kalari.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.entity.MisPropertyPayOption;

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
