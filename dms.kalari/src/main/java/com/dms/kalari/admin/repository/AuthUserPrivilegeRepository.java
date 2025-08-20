package com.dms.kalari.admin.repository;

import com.dms.kalari.admin.entity.AuthUserPrivilege;
import com.dms.kalari.admin.dto.UserPrivilegeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    // Preload all privileges for role (optionally filter by inst if you use it)
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


    // If you also need institute scoping:
    /*@Query("""
        SELECT new com.dms.kalari.admin.dto.UserPrivilegeProjection(
            up.userPrivilegeId,
            up.roleId,
            up.moduleId,
            up.operationUniqueId,
            up.appPage.appPageId,
            op.operationId,
            op.alias,
            op.realPath
        )
        FROM AuthUserPrivilege up
        JOIN up.operation op
        WHERE up.roleId = :roleId AND (:instId IS NULL OR up.instId = :instId)
    """)
    List<UserPrivilegeProjection> findPrivilegesByRoleAndInst(@Param("roleId") Long roleId,
                                                              @Param("instId") Long instId);*/
}
