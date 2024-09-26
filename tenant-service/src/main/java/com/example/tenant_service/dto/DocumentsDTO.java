package com.example.tenant_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsDTO {

    // Fields from MisDocuments with validations

    private Long docId; // Optional for updates; not needed for new entries.

    @NotNull(message = "Document type is required.")
    private Short docType;

    @NotNull(message = "Document reference type is required.")
    private Short docRefType;

    @NotNull(message = "Document reference ID is required.")
    private Long docRefId;


    @NotNull(message = "Document number is required.")
    @NotBlank(message = "Document number cannot be blank.")
    @Size(max = 50, message = "Document number cannot exceed 50 characters.")
    private String docNo;

    @NotNull(message = "Document description is required.")
    @NotBlank(message = "Document description cannot be blank.")
    @Size(max = 255, message = "Document description cannot exceed 255 characters.")
    private String docDesc;


    @Size(max = 255, message = "Document remarks cannot exceed 255 characters.")
    private String docRemarks;

    @Size(max = 100, message = "Document issuing authority cannot exceed 100 characters.")
    private String docIssueAuth;

    @NotNull(message = "Application Apply Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docApplyDate;

    @NotNull(message = "Application Issue Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docIssueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate docExpiryDate;  // Optional field

    private Short docAlertDays = 0; // Default value

    @NotNull(message = "Document amount is required.")
    private BigDecimal docAmount;

    @NotNull(message = "Document tax is required.")
    private BigDecimal docTax;

    @Size(max = 50, message = "Payment details cannot exceed 50 characters.")
    private String docPaydet;

    @NotNull(message = "Tenant ID is required.")
    private Long docTntId;
    
    // New field for tracking document renewals
    private Long docAgreement; // Holds the same value for all versions of the document

}
