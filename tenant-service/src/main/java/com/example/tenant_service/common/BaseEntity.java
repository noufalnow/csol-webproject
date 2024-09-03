package com.example.tenant_service.common;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "u_created")
    private Long uCreated;

    @Column(name = "t_created", nullable = false)
    private LocalDateTime tCreated;

    @Column(name = "t_modified")
    private LocalDateTime tModified;

    @Column(name = "u_modified")
    private Long uModified;

    @Column(name = "u_deleted")
    private Long uDeleted;

    @Column(name = "t_deleted")
    private LocalDateTime tDeleted;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "active", nullable = false)
    private Short active = 0;

    @PrePersist
    protected void onCreate() {
        uCreated = 10L;
        tCreated = LocalDateTime.now();
        tModified = tCreated;
        active = 1;
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        tModified = LocalDateTime.now();
        uModified = 10L;
    }

    @PreRemove
    protected void onDelete() {
        uDeleted = 10L;
        tDeleted = LocalDateTime.now();
        deleted = false;
    }
}
