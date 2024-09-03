package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "mis_property_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisPropertyStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psts_id")
    private Long pstsId;

    @NotNull(message = "Status type is mandatory")
    @Column(name = "psts_type", nullable = false)
    private Short pstsType = 1;

    @NotNull(message = "Property ID is mandatory")
    @Column(name = "psts_prop_id", nullable = false)
    private Long pstsPropId;

    @NotBlank(message = "Remarks are mandatory")
    @Column(name = "psts_remarks", nullable = false)
    private String pstsRemarks;

    @NotNull(message = "Status date is mandatory")
    @Column(name = "psts_status_date", nullable = false)
    private Date pstsStatusDate;

    @Column(name = "psts_attach_prop")
    private Long pstsAttachProp;
}


