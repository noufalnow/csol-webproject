package com.dms.kalari.events.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateFileRecord {
    private String file;
    private LocalDateTime createdAt;
    private String status; // e.g. "ACTIVE", "REVOKED"
}

