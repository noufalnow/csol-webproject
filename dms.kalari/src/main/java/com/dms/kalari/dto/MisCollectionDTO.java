package com.dms.kalari.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisTenants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisCollectionDTO extends BaseDTO {

    private Long collId;
    private Long collType;
    private Long collCust;
    private BigDecimal collAmount;  // Change from Double to BigDecimal
    private Short collCollMode;
    private String collChqNo;
    private String collRemarks;
    private LocalDate collPayDate;
    private String collRefNo;
    private String collFileNo;
    private LocalDate collAppDate;
    private Long collAppBy;
    private String collAppNote;
    private Short collAppStatus;
    
    
    private Long tenantId;
    private BigDecimal totalAmount;
    private BigDecimal collDiscount = BigDecimal.ZERO; 
    private BigDecimal netAmount;
    private List<Long> selectedSchedules;  // List of selected payment option IDs
    
    private MisTenants misTenants;
    private MisDocuments misDocuments;
}
