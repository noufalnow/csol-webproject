package com.dms.kalari.admin.entity;

import java.time.LocalDateTime;

import com.dms.kalari.common.BaseEntity;
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

    @Column(name = "alias", nullable = false, length = 255) // Database says NOT NULL
    private String alias;

    @Column(name = "realpath", nullable = false, length = 255) // Fixed column name and length
    private String realPath;

    @Column(name = "operation", nullable = false, length = 50)
    private String operation;
}

