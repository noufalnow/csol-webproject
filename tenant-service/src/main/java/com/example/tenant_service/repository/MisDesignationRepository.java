package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisDesignation;
import com.example.tenant_service.common.BaseRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MisDesignationRepository extends BaseRepository<MisDesignation, Long> {
    
    @Query("SELECT b FROM MisDesignation b WHERE b.id = :bldId AND b.deleted = false")
    Optional<MisDesignation> findByIdAndNotDeleted(Long bldId);

    @Query("SELECT b FROM MisDesignation b WHERE b.deleted = false")
    Page<MisDesignation> findAllNotDeleted(String search, Pageable pageable);
}

