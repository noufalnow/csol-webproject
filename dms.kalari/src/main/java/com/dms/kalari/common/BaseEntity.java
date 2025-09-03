package com.dms.kalari.common;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.dms.kalari.security.CustomUserPrincipal;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dms.kalari.security.CustomUserPrincipal;

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
    
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.getUserId();
        }
        return null;
    }


    @PrePersist
    protected void onCreate() {
        uCreated = getCurrentUserId();;
        tCreated = LocalDateTime.now();
        active = 1;
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        tModified = LocalDateTime.now();
        uModified = getCurrentUserId();;
    }

    @PreRemove
    protected void onDelete() {
        uDeleted = getCurrentUserId();;
        tDeleted = LocalDateTime.now();
        deleted = true;
    }
}
