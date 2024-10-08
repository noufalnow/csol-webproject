package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mis_property_payoption")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisPropertyPayOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popt_id")
    private Long poptId;

    @NotNull(message = "Property ID is required.")
    @Column(name = "popt_prop_id", nullable = false)
    private Long poptPropId;

    @Column(name = "popt_doc_id")
    private Long poptDocId;

    @NotNull(message = "Payment type is required.")
    @Column(name = "popt_type", nullable = false)
    private Short poptType;

    @NotNull(message = "Payment date is required.")
    //@PastOrPresent(message = "Payment date cannot be in the future.")
    @Column(name = "popt_date", nullable = false)
    private LocalDate poptDate;

    @NotNull(message = "Payment amount is required.")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero.")
    @Column(name = "popt_amount", precision = 13, scale = 3, nullable = false)
    private BigDecimal poptAmount;

    @Column(name = "popt_bank")
    private Short poptBank;

    @Size(max = 20, message = "Cheque number cannot exceed 20 characters.")
    @Column(name = "popt_chqno", length = 20)
    private String poptChqno;

    @NotNull(message = "Payment status is required.")
    @Column(name = "popt_status", nullable = false, columnDefinition = "smallint default 1")
    private Short poptStatus = 1;

    @PastOrPresent(message = "Status date cannot be in the future.")
    @Column(name = "popt_status_date")
    private LocalDate poptStatusDate;
}
