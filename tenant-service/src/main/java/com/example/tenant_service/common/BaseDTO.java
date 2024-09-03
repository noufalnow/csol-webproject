package com.example.tenant_service.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO {
    private Long uCreated;
    private LocalDateTime tCreated;
    private LocalDateTime tModified;
    private Long uModified;
    private Long uDeleted;
    private LocalDateTime tDeleted;
    private Short deleted;
    private Short active;
}
