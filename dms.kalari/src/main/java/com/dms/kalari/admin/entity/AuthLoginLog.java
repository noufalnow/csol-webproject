package com.dms.kalari.admin.entity;
import java.time.LocalDateTime;

import com.dms.kalari.common.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_login_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "login_time", 
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", 
    insertable = false, 
    updatable = false)
    private LocalDateTime loginTime;

}
