package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mis_building")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisBuilding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bld_id")
    private Long bldId;

    @NotBlank(message = "Building name is mandatory")
    @Column(name = "bld_name", nullable = false)
    private String bldName;

    @NotBlank(message = "Building number is mandatory")
    @Column(name = "bld_no", nullable = false)
    private String bldNo;

    @NotBlank(message = "Area is mandatory")
    @Column(name = "bld_area", nullable = false)
    private String bldArea;

    @NotBlank(message = "Block number is mandatory")
    @Column(name = "bld_block_no", nullable = false)
    private String bldBlockNo;

    @Column(name = "bld_plot_no")
    private String bldPlotNo;

    @NotBlank(message = "Way is mandatory")
    @Column(name = "bld_way", nullable = false)
    private String bldWay;

    @NotBlank(message = "Street is mandatory")
    @Column(name = "bld_street", nullable = false)
    private String bldStreet;

    @NotBlank(message = "Block is mandatory")
    @Column(name = "bld_block", nullable = false)
    private String bldBlock;
}
