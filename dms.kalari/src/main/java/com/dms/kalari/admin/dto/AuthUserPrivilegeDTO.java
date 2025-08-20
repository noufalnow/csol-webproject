package com.dms.kalari.admin.dto;

import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.admin.dto.AppPageOperationDTO;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthUserPrivilegeDTO extends BaseDTO {
    private Long userPrivilegeId;
    private Long instId;
    
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    @NotNull(message = "Module ID is required")
    private Integer moduleId; // Changed from Long to Integer
    
    @NotNull(message = "App page ID is required")
    private Long appPageId; // Renamed from appPagesUniqueId
    
    private AuthAppPageDTO appPage;
    
    @NotNull(message = "Operation ID is required")
    private Long operationId; // Renamed from operationUniqueId
    
    private AppPageOperationDTO operation;
}
