package com.dms.kalari.admin.entity;
import java.time.LocalDateTime;

import com.dms.kalari.common.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "user_action_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLogAction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private Long loginId;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_data", columnDefinition = "text")
    private String requestData;

    @Column(name = "action_time", 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", 
            insertable = false, 
            updatable = false)
    private LocalDateTime actionTime;
    

}
