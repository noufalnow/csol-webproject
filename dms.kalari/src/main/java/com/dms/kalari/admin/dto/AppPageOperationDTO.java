package com.dms.kalari.admin.dto;

import jakarta.persistence.*;
import java.time.Instant;

import com.dms.kalari.common.BaseDTO;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppPageOperationDTO extends BaseDTO {
    private Long operationId;
    
    @NotNull(message = "App page ID is required")
    private Long appPageId;
    
    @NotBlank(message = "Alias is required")
    @Size(max = 255, message = "Alias must be less than 255 characters")
    private String alias;
    
    @NotBlank(message = "Operation is required")
    @Size(max = 50, message = "Operation must be less than 50 characters")
    private String operation;
    
    @NotBlank(message = "Real path is required")
    @Size(max = 255, message = "Real path must be less than 255 characters")
    private String realPath;
}
