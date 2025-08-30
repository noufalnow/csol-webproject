package com.dms.kalari.admin.dto;

import lombok.Data;

@Data
public class OperationWithPermission {
    private Long operationId;
    private String operationName;
    private String alias;
    private boolean hasPermission;
}