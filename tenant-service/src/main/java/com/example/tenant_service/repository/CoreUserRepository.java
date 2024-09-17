package com.example.tenant_service.repository;

import com.example.tenant_service.common.BaseRepository;
import com.example.tenant_service.entity.CoreUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.tenant_service.entity.MisDesignation;

@Repository
public interface CoreUserRepository extends BaseRepository<CoreUser, Long> {

    @Query("SELECT u FROM CoreUser u WHERE u.id = :userId AND u.deleted = false")
    Optional<CoreUser> findByIdAndNotDeleted(Long userId);

    // Fetch non-deleted users with pagination and sorting
    @Query("SELECT u FROM CoreUser u WHERE u.deleted = false AND "
           + "(LOWER(u.userFname) LIKE LOWER(CONCAT('%', :search, '%')) "
           + "OR LOWER(u.userLname) LIKE LOWER(CONCAT('%', :search, '%')) "
           + "OR LOWER(u.userUname) LIKE LOWER(CONCAT('%', :search, '%')) "
           + "OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CoreUser> findAllNotDeleted(String search, Pageable pageable);
    
    
    List<CoreUser> findByDesignation(MisDesignation designation);
}


