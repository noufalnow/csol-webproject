package com.dms.kalari.admin.entity;

import java.time.LocalDateTime;

import com.dms.kalari.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "auth_appmenus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthAppMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appmenuid")
    private Long appMenuId;

    @Column(name = "menuname", nullable = false, length = 50)
    private String menuName;

    @Column(name = "ordernumber", nullable = false)
    private Integer orderNumber;

}
