package com.dms.kalari.admin.repository;

import com.dms.kalari.admin.entity.AuthAppPageOperation;
import com.dms.kalari.admin.entity.AuthUserPrivilege;
import com.dms.kalari.admin.dto.UserPrivilegeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface AuthUserPrivilegeRepository extends JpaRepository<AuthUserPrivilege, Long> {

    @Query("SELECT COUNT(up) FROM AuthUserPrivilege up " +
           "WHERE up.roleId = :roleId " +
           "AND (:instId IS NULL OR up.instId = :instId) " +
           "AND up.appPage.appPageId = :appPageId " +
           "AND up.operation.operationId = :operationId")
    long countActivePrivilege(@Param("roleId") Long roleId,
                              @Param("instId") Long instId,
                              @Param("appPageId") Long appPageId,
                              @Param("operationId") Long operationId);

    // Preload all privileges for role
    @Query("""
        SELECT 
            op.operationId as operationId,
            op.alias as alias,
            op.realPath as realPath,
            up.roleId as roleId,
            up.moduleId as moduleId,
            up.appPage.appPageId as appPageId,
            up.userPrivilegeId as userPrivilegeId
        FROM AuthUserPrivilege up
        JOIN up.operation op
        WHERE up.roleId = :roleId
    """)
    List<UserPrivilegeProjection> findPrivilegesByRole(@Param("roleId") Long roleId);

    // Get privileges by role and module
    /*@Query("""
        SELECT up FROM AuthUserPrivilege up
        WHERE up.roleId = :roleId AND up.moduleId = :moduleId
    """)
    List<AuthUserPrivilege> findByRoleIdAndModuleId(@Param("roleId") Long roleId, 
                                                   @Param("moduleId") Integer moduleId);*/

    @Query(value = """
        SELECT res.apppageid as appPageId,
               CASE WHEN res.operation_count <= COALESCE(per.per_count, 0) THEN true ELSE false END as hasAllPermissions
        FROM (
            SELECT ap.apppageid, COUNT(ao.operationid) as operation_count
            FROM auth_apppages ap
            JOIN auth_apppagesoperations ao ON ap.apppageid = ao.apppageid
            WHERE ao.active = 1
            GROUP BY ap.apppageid
        ) res
        LEFT JOIN (
            SELECT apppageid, COUNT(*) as per_count
            FROM auth_userprivilages
            WHERE roleid = :roleId AND moduleid = :moduleId
            GROUP BY apppageid
        ) per ON per.apppageid = res.apppageid
    """, nativeQuery = true)
    List<Object[]> countPagePermissions(@Param("roleId") Long roleId, 
                                       @Param("moduleId") Long moduleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AuthUserPrivilege up WHERE up.roleId = :roleId AND up.moduleId = :moduleId " +
           "AND (up.roleId <> :excludeRoleId OR up.appPage.appPageId <> :excludePageId)")
    void deleteByRoleIdAndModuleIdWithExclusions(@Param("roleId") Long roleId, 
                                                @Param("moduleId") Long moduleId,
                                                @Param("excludeRoleId") Long excludeRoleId,
                                                @Param("excludePageId") Long excludePageId);

    // Get module resources (pages and operations)
    /*@Query("""
        SELECT DISTINCT up FROM AuthUserPrivilege up
        JOIN FETCH up.appPage ap
        JOIN FETCH up.operation op
        WHERE up.moduleId = :moduleId
        ORDER BY ap.appPageId, op.operationId
    """)
    List<AuthUserPrivilege> findModuleResources(@Param("moduleId") Integer moduleId);*/
    
    
    @Query("""
            SELECT up FROM AuthUserPrivilege up
            JOIN FETCH up.appPage ap
            JOIN FETCH up.operation op
            WHERE up.roleId = :roleId AND up.moduleId = :moduleId
        """)
        List<AuthUserPrivilege> findByRoleIdAndModuleId(@Param("roleId") Long roleId, 
                                                       @Param("moduleId") Long moduleId);
        
    @Query("""
    	    SELECT o FROM AuthAppPageOperation o
    	    JOIN o.appPage p
    	    WHERE p.menu.appMenuId = :moduleId
    	    AND o.active = 1
    	    AND o.pageOperationLevel LIKE '%' || :level || '%'
    	    ORDER BY p.pageName, p.appPageId, o.alias
    	""")
    	List<AuthAppPageOperation> findOperationsByModuleId(
    	    @Param("moduleId") Long moduleId,
    	    @Param("level") String level);

    
    
    
    @Query("""
            SELECT DISTINCT up FROM AuthUserPrivilege up
            JOIN FETCH up.appPage ap
            JOIN FETCH up.operation op
            WHERE up.moduleId = :moduleId
            ORDER BY ap.pageName, ap.appPageId, op.alias
        """)
        List<AuthUserPrivilege> findModuleResources(@Param("moduleId") Long moduleId); 
            
 
    
    @Query("""
            SELECT DISTINCT CONCAT(up.appPage.appPageId, '_', up.operation.operationId)
            FROM AuthUserPrivilege up
            WHERE up.roleId = :roleId
              AND up.moduleId = :moduleId
        """)
        Set<String> findRolePermissionKeys(@Param("roleId") Long roleId,
                                           @Param("moduleId") Long moduleId);

        @Query("""
            SELECT o FROM AuthAppPageOperation o
            JOIN FETCH o.appPage ap
            WHERE ap.menu.appMenuId = :moduleId AND o.active = 1
            ORDER BY o.operationName, ap.appPageId, o.alias
        """)
        List<AuthAppPageOperation> findOperationsByModuleIdWithPage(@Param("moduleId") Long moduleId);

    
}