package com.dms.kalari.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;  // Import statement for List

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.dms.kalari.common.BaseEntity;

@Entity
@Table(name = "mis_designation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class MisDesignation extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "desig_id")
    private Long desigId;

    @Column(name = "desig_code", nullable = false)
    private String desigCode;

    @Column(name = "desig_name", nullable = false)
    private String desigName;
    
    @OneToMany(mappedBy = "designation")
    private List<CoreUser> users;

    @Column(name = "desig_level")
    private Long desigLevel;

    @Column(name = "desig_type")
    private Short desigType;
}
