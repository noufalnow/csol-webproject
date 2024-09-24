package com.example.tenant_service.repository;


import com.example.tenant_service.entity.MisBuilding;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.tenant_service.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisBuildingRepository extends BaseRepository<MisBuilding, Long> {

    @Query("SELECT b FROM MisBuilding b WHERE b.id = :bldId AND b.deleted = false")
    Optional<MisBuilding> findByIdAndNotDeleted(Long bldId);
    
    
    // Find all designations that are not deleted
    List<MisBuilding> findAllByDeletedFalse();

    @Query("SELECT b FROM MisBuilding b WHERE b.deleted = false")
    Page<MisBuilding> findAllNotDeleted(String search, Pageable pageable);    
    
}
