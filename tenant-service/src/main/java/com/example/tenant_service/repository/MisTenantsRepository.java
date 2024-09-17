package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisTenants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.tenant_service.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisTenantsRepository extends BaseRepository<MisTenants, Long> {

    @Query("SELECT t FROM MisTenants t WHERE t.id = :tntId AND t.deleted = false")
    Optional<MisTenants> findByIdAndNotDeleted(Long tntId);

    @Query("SELECT t FROM MisTenants t WHERE t.deleted = false")
    Page<MisTenants> findAllNotDeleted(String search, Pageable pageable);
}
