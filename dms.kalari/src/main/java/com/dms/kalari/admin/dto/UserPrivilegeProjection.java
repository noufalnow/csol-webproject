package com.dms.kalari.admin.dto;

public interface UserPrivilegeProjection {
    Long getUserPrivilegeId();
    Long getRoleId();
    Long getModuleId();
    Long getAppPageId();
    Long getOperationId();
    String getAlias();
    String getRealPath();
}
