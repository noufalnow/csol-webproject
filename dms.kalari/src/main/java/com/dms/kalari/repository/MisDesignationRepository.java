package com.dms.kalari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseRepository;

@Repository
public interface MisDesignationRepository extends BaseRepository<MisDesignation, Long> {
    
    @Query("SELECT b FROM MisDesignation b WHERE b.id = :desigId AND b.deleted = false")
    Optional<MisDesignation> findByIdAndNotDeleted(Long desigId);
    
    // Find all designations that are not deleted
    List<MisDesignation> findAllByDeletedFalse();

    @Query("SELECT b FROM MisDesignation b WHERE b.deleted = false")
    Page<MisDesignation> findAllNotDeleted(String search, Pageable pageable);
    
    List<MisDesignation> findByDeletedFalseAndDesigType(Short desigType);
    
    List<MisDesignation> findByDeletedFalseAndDesigLevel(Node.Type desigLevel);
    
    List<MisDesignation> findByDeletedFalseAndDesigLevelAndDesigType(Node.Type desigLevel ,Short desigType);
    
}

