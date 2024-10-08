package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "mis_collection_det")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisCollectionDet extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cdet_id")
	private Long cdetId;

	@Column(name = "cdet_coll_id")
	private Long cdetCollId;

	@Column(name = "cdet_popt_id")
	private Long cdetPoptId;

	@Column(name = "cdet_amt_topay", precision = 13, scale = 3)
	private BigDecimal cdetAmtToPay;

	@Column(name = "cdet_amt_paid", precision = 13, scale = 3)
	private BigDecimal cdetAmtPaid;
	
    /*// Assuming a ManyToOne relationship with MisCollection entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cdet_coll_id", insertable = false, updatable = false)  // Assumes FK column is cdet_coll_id
    private MisCollection misCollection;  // Add this field to reference MisCollection

    
    // Define the relationship to MisPropertyPayoption (assuming Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cdet_popt_id", insertable = false, updatable = false)
    private MisPropertyPayOption misPropertyPayoption;  // Add this field*/

}
