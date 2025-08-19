package com.dms.kalari.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "v_documents") // Assuming the view is called 'v_documents'
@Data
@NoArgsConstructor
@AllArgsConstructor
@Immutable // Since this is a view and should not be modified
public class MisDocumentsView {

    // Fields from MisDocuments
    @Id
    @Column(name = "doc_id")
    private Long docId;

    @Column(name = "doc_type")
    private Short docType;

    @Column(name = "doc_ref_type")
    private Short docRefType;

    @Column(name = "doc_ref_id")
    private Long docRefId;

    @Column(name = "doc_no")
    private String docNo;

    @Column(name = "doc_desc")
    private String docDesc;

    @Column(name = "doc_remarks")
    private String docRemarks;

    @Column(name = "doc_issue_auth")
    private String docIssueAuth;

    @Column(name = "doc_apply_date")
    private Date docApplyDate;

    @Column(name = "doc_issue_date")
    private Date docIssueDate;

    @Column(name = "doc_expiry_date")
    private Date docExpiryDate;

    @Column(name = "doc_alert_days")
    private Short docAlertDays;

    @Column(name = "doc_amount")
    private BigDecimal docAmount;

    @Column(name = "doc_tax")
    private BigDecimal docTax;

    @Column(name = "doc_paydet")
    private String docPaydet;

    @Column(name = "doc_tnt_id")
    private Long docTntId;
    
    @Column(name = "doc_agreement")
    private Long docAgreement;
    
    
    
    
    private boolean deleted; // Make sure this field exists

    // Fields from MisTenants
    @Column(name = "tnt_full_name")
    private String tenantFullName;

    @Column(name = "tnt_comp_name")
    private String tenantCompanyName;

    @Column(name = "tnt_phone")
    private String tenantPhone;

    @Column(name = "tnt_tele")
    private String tenantTele;

    @Column(name = "tnt_id_no")
    private String tenantIdNo;

    @Column(name = "tnt_crno")
    private String tenantCrno;

    @Column(name = "tnt_expat")
    private Short tenantExpat;

    @Column(name = "tnt_agr_type")
    private Short tenantAgrType;

    @Column(name = "tnt_doc_id")
    private Long tenantDocId;

    // Fields from MisProperty
    @Column(name = "prop_id")
    private Long propId;

    @Column(name = "prop_no")
    private String propNo;

    @Column(name = "prop_name")
    private String propName;

    @Column(name = "prop_fileno")
    private String propFileno;

    @Column(name = "prop_building")
    private Short propBuilding;

    @Column(name = "prop_responsible")
    private Long propResponsible;

    @Column(name = "prop_remarks")
    private String propRemarks;

    @Column(name = "prop_cat")
    private Short propCat;

    @Column(name = "prop_type")
    private Short propType;

    @Column(name = "prop_level")
    private Short propLevel;

    @Column(name = "prop_elec_meter")
    private String propElecMeter;

    @Column(name = "prop_water")
    private String propWater;

    @Column(name = "prop_building_type")
    private Short propBuildingType;

    @Column(name = "prop_status")
    private Long propStatus;

    @Column(name = "prop_elec_account")
    private String propElecAccount;

    @Column(name = "prop_elec_recharge")
    private String propElecRecharge;

    @Column(name = "prop_account")
    private Long propAccount;
}
