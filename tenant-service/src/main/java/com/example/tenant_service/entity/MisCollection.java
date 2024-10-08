package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "mis_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisCollection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coll_id")
    private Long collId;

    @Column(name = "coll_type", nullable = false)
    private Long collType;

    @Column(name = "coll_cust", nullable = false)
    private Long collCust;  // This is for customer

    @Column(name = "coll_property")  // Add this if you want to filter by property
    private String collProperty;  // Assuming it's a String. Adjust as necessary.

    @Column(name = "coll_amount", nullable = false, precision = 13, scale = 3)
    private BigDecimal collAmount;

    @Column(name = "coll_discount", precision = 13, scale = 3)
    private BigDecimal collDiscount = BigDecimal.ZERO;

    @Column(name = "coll_coll_mode", nullable = false)
    private Short collCollMode = 1;

    @Column(name = "coll_chqno")
    private String collChqNo;

    @Column(name = "coll_remarks")
    private String collRemarks;

    @Column(name = "coll_paydate", nullable = false)
    private java.time.LocalDate collPayDate;

    @Column(name = "coll_refno")
    private String collRefNo;

    @Column(name = "coll_file_no")
    private String collFileNo;

    @Column(name = "coll_app_date")
    private java.time.LocalDate collAppDate;

    @Column(name = "coll_app_by")
    private Long collAppBy;

    @Column(name = "coll_app_note")
    private String collAppNote;

    @Column(name = "coll_app_status", nullable = false)
    private Short collAppStatus = 1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coll_cust", referencedColumnName = "tnt_id", insertable = false, updatable = false)
    private MisTenants misTenants;
    
 
}
