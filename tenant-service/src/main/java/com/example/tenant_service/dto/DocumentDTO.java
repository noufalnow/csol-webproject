package com.example.tenant_service.dto;

import com.example.tenant_service.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO extends BaseDTO {
    private Long docId;
    private Short docType;
    private Short docRefType;
    private Long docRefId;
    private String docNo;
    private String docDesc;
    private String docRemarks;
    private String docIssueAuth;
    private Date docApplyDate;
    private Date docIssueDate;
    private Date docExpiryDate;
    private Short docAlertDays;
    private BigDecimal docAmount;
    private BigDecimal docTax;
    private String docPaydet;
    private Long docTntId;
}
