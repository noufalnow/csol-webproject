package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mis_property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisProperty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prop_id")
    private Long propId;

    @NotBlank(message = "Property number is mandatory")
    @Column(name = "prop_no", nullable = false)
    private String propNo;

    @NotBlank(message = "Property name is mandatory")
    @Column(name = "prop_name", nullable = false)
    private String propName;

    @NotBlank(message = "File number is mandatory")
    @Column(name = "prop_fileno", nullable = false)
    private String propFileno;

    @NotNull(message = "Building ID is mandatory")
    @Column(name = "prop_building", nullable = false)
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

    @Column(name = "prop_status", nullable = false)
    private Long propStatus = 1L;

    @Column(name = "prop_elec_account")
    private String propElecAccount;

    @Column(name = "prop_elec_recharge")
    private String propElecRecharge;

    @Column(name = "prop_account")
    private Long propAccount;
}
