package com.dms.kalari.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.common.BaseRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface CoreUserRepository extends BaseRepository<CoreUser, Long> {

    // Fetch user with all related entities to avoid N+1
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "LEFT JOIN FETCH u.userNode.parent " +
           "WHERE u.id = :userId AND u.deleted = false")
    Optional<CoreUser> findByIdAndNotDeleted(@Param("userId") Long userId);

    // Fetch user by email with all related entities
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "LEFT JOIN FETCH u.userNode.parent " +
           "WHERE u.userEmail = :userEmail AND u.deleted = false")
    Optional<CoreUser> findByUserEmail(@Param("userEmail") String userEmail);

    // Fetch non-deleted users with pagination, sorting and related entities
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "WHERE u.deleted = false AND " +
           "(LOWER(u.userFname) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.userLname) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.userUname) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.designation.desigName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CoreUser> findAllNotDeleted(@Param("search") String search, Pageable pageable);

    // Fetch users by designation with related entities
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "WHERE u.designation = :designation AND u.deleted = false")
    List<CoreUser> findByDesignation(@Param("designation") MisDesignation designation);

    // Fetch users by node ID with related entities
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "WHERE u.userNode.id = :nodeId AND u.deleted = false")
    List<CoreUser> findByUserNodeIdAndNotDeleted(@Param("nodeId") Long nodeId);

    // Fetch users by node ID and type with related entities
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "WHERE u.userNode.id = :nodeId AND u.userType = :userType AND u.deleted = false " +
		   "ORDER BY u.userFname ASC")
    List<CoreUser> findByUserNodeIdAndTypeAndNotDeleted(@Param("nodeId") Long nodeId, 
                                                       @Param("userType") UserType userType);

    // Bulk fetch users by IDs with all related entities (useful for batch processing)
    @Query("SELECT u FROM CoreUser u " +
           "LEFT JOIN FETCH u.designation " +
           "LEFT JOIN FETCH u.userNode " +
           "LEFT JOIN FETCH u.userNode.parent " +
           "WHERE u.id IN :userIds AND u.deleted = false")
    List<CoreUser> findByIdsInWithRelations(@Param("userIds") List<Long> userIds);
}