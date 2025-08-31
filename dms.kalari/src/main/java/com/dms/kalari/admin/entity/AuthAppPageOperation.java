package com.dms.kalari.admin.entity;

import java.util.List;

import com.dms.kalari.common.BaseEntity;
import com.dms.kalari.branch.entity.Node;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_apppagesoperations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthAppPageOperation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operationid")
    private Long operationId;

    @ManyToOne
    @JoinColumn(name = "apppageid", referencedColumnName = "apppageid", nullable = false)
    private AuthAppPage appPage;

    @Column(name = "alias", nullable = false, length = 255)
    private String alias;

    @Column(name = "realpath", nullable = false, length = 255)
    private String realPath;

    @Column(name = "operation", nullable = false, length = 50)
    private String operation;
    
    @Column(name = "operation_name", length = 100)
    private String operationName;
    

    @Column(name = "page_operation_level", length = 250)
    private String pageOperationLevel;
}