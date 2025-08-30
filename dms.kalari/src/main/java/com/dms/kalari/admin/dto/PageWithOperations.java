package com.dms.kalari.admin.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageWithOperations {
    private Long pageId;
    private String pageName;
    private List<OperationWithPermission> operations;
    private boolean hasAllPermissions;
}

