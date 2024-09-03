package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "mis_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisDocuments extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;

    @NotNull(message = "Document type is mandatory")
    @Column(name = "doc_type", nullable = false)
    private Short docType;

    @NotNull(message = "Reference type is mandatory")
    @Column(name = "doc_ref_type", nullable = false)
    private Short docRefType;

    @NotNull(message = "Reference ID is mandatory")
    @Column(name = "doc_ref_id", nullable = false)
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

    @Column(name = "doc_alert_days", nullable = false)
    private Short docAlertDays = 0;

    @Column(name = "doc_amount")
    private BigDecimal docAmount;

    @Column(name = "doc_tax")
    private BigDecimal docTax;

    @Column(name = "doc_paydet")
    private String docPaydet;

    @NotNull(message = "Tenant ID is mandatory")
    @Column(name = "doc_tnt_id", nullable = false)
    private Long docTntId;
}
