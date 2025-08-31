package com.dms.kalari.admin.entity;

import java.time.LocalDateTime;

import com.dms.kalari.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_userprivilages")
@Data
public class AuthUserPrivilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userprivilageid")
    private Long userPrivilegeId;

    @Column(name = "instid")
    private Long instId;

    @Column(name = "roleid", nullable = false)
    private Long roleId;

    @Column(name = "moduleid", nullable = false)
    private Long moduleId;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apppageid", nullable = false)
    private AuthAppPage appPage;


    @ManyToOne
    @JoinColumn(name = "operationuniqueid", referencedColumnName = "operationid", nullable = false) // FIXED
    private AuthAppPageOperation operation;
}
