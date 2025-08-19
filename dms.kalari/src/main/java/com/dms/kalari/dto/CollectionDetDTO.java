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

public class CollectionDetDTO {
    private Long cdetId;
    private Long cdetCollId;
    private Long cdetPoptId;
    private BigDecimal cdetAmtToPay;
    private BigDecimal cdetAmtPaid;
    
    private MisPropertyPayOption misPropertyPayoption;
}
