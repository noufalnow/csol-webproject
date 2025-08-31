// PageWithOperations.java
package com.dms.kalari.admin.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PageWithOperations {
    private Long pageId;
    private String pageName;
    private List<OperationWithPermission> operations = new ArrayList<>();
    private boolean hasAllPermissions;
    
    public void calculateHasAllPermissions() {
        this.hasAllPermissions = !operations.isEmpty() && 
            operations.stream().allMatch(OperationWithPermission::isHasPermission);
    }
    
    // Convenience constructor
    public PageWithOperations(Long pageId, String pageName) {
        this.pageId = pageId;
        this.pageName = pageName;
    }
    
    // Add operation method
    public void addOperation(OperationWithPermission operation) {
        this.operations.add(operation);
    }
}