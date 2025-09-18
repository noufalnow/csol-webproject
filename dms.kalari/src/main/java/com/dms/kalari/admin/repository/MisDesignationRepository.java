package com.dms.kalari.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseRepository;

import feign.Param;

@Repository
public interface MisDesignationRepository extends BaseRepository<MisDesignation, Long> {
    
    @Query("SELECT b FROM MisDesignation b WHERE b.id = :desigId AND b.deleted = false")
    Optional<MisDesignation> findByIdAndNotDeleted(Long desigId);
    
    // Find all designations that are not deleted
    List<MisDesignation> findAllByDeletedFalse();

    @Query("""
    	       SELECT md FROM MisDesignation md
    	       WHERE md.deleted = false
    	         AND (:code IS NULL OR :code = '' OR LOWER(md.desigCode) LIKE LOWER(CONCAT('%', :code, '%')))
    	         AND (:name IS NULL OR :name = '' OR LOWER(md.desigName) LIKE LOWER(CONCAT('%', :name, '%')))
    	         AND (:level IS NULL OR md.desigLevel = :level)
    	         AND (:type IS NULL OR md.desigType = :type)
    	       """)
    	Page<MisDesignation> findAllNotDeleted(@Param("code")  String code,
    	                                       @Param("name")  String name,
    	                                       @Param("level") Node.Type level,
    	                                       @Param("type")  Short type,
    	                                       Pageable pageable);


 
    List<MisDesignation> findByDeletedFalseAndDesigType(Short desigType);
    
    List<MisDesignation> findByDeletedFalseAndDesigLevel(Node.Type desigLevel);
    
    List<MisDesignation> findByDeletedFalseAndDesigLevelAndDesigType(Node.Type desigLevel ,Short desigType);
    
}

