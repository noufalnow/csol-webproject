package com.dms.kalari.admin.entity;


import com.dms.kalari.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_apppages")
@Data
public class AuthAppPage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apppageid")
    private Long appPageId;

    @ManyToOne
    @JoinColumn(name = "menuuniqueid", referencedColumnName = "appmenuid", nullable = false) // FIXED
    private AuthAppMenu menu;

    @Column(name = "pagename", nullable = false, length = 50)
    private String pageName;
}
