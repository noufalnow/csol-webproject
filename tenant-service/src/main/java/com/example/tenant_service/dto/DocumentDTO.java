package com.example.tenant_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    // Fields from MisDocuments
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
    private Short docAlertDays = 0;
    private BigDecimal docAmount;
    private BigDecimal docTax;
    private String docPaydet;
    private Long docTntId;

    // Additional fields from MisDocumentsView (for tenant and property information)
    private boolean deleted; // Ensure to include this field

    // Fields from MisTenants
    private String tenantFullName;
    private String tenantCompanyName;
    private String tenantPhone;
    private String tenantTele;
    private String tenantIdNo;
    private String tenantCrno;
    private Short tenantExpat;
    private Short tenantAgrType;
    private Long tenantDocId;

    // Fields from MisProperty
    private Long propId;
    private String propNo;
    private String propName;
    private String propFileno;
    private Short propBuilding;
    private Long propResponsible;
    private String propRemarks;
    private Short propCat;
    private Short propType;
    private Short propLevel;
    private String propElecMeter;
    private String propWater;
    private Short propBuildingType;
    private Long propStatus;
    private String propElecAccount;
    private String propElecRecharge;
    private Long propAccount;
}
