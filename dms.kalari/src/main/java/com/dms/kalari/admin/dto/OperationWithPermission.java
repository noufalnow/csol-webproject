// OperationWithPermission.java
package com.dms.kalari.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationWithPermission {
    private Long operationId;
    private String operationName;
    private String alias;
    private boolean hasPermission;
}