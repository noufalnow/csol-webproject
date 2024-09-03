package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mis_tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisTenants extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tnt_id")
    private Long tntId;

    @NotBlank(message = "Tenant full name is mandatory")
    @Column(name = "tnt_full_name", nullable = false)
    private String tntFullName;

    @Column(name = "tnt_comp_name")
    private String tntCompName;

    @Column(name = "tnt_phone")
    private String tntPhone;

    @Column(name = "tnt_tele")
    private String tntTele;

    @NotBlank(message = "Tenant ID number is mandatory")
    @Column(name = "tnt_id_no", nullable = false)
    private String tntIdNo;

    @NotBlank(message = "Tenant CR number is mandatory")
    @Column(name = "tnt_crno", nullable = false)
    private String tntCrno;

    @Column(name = "tnt_expat")
    private Short tntExpat;

    @Column(name = "tnt_agr_type")
    private Short tntAgrType;

    @Column(name = "tnt_doc_id")
    private Long tntDocId;
}
