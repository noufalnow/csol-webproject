package com.dms.kalari.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.dto.CustomerPaymentDTO;
import com.dms.kalari.entity.MisCollection;
import com.dms.kalari.entity.MisCollectionDet;

import org.springframework.data.repository.query.Param;

@Repository
public interface MisCollectionRepository extends BaseRepository<MisCollection, Long> {
	
    @Query("SELECT b FROM MisCollection b WHERE b.id = :bldId AND b.deleted = false")
    Optional<MisCollection> findByIdAndNotDeleted(Long bldId);
    
    // Find all designations that are not deleted
    List<MisCollection> findAllByDeletedFalse();

    @Query("SELECT b FROM MisCollection b WHERE b.deleted = false")
    Page<MisCollection> findAllNotDeleted(String search, Pageable pageable);  
    
    
    @Query("SELECT mc FROM MisCollection mc " +
    	       "LEFT JOIN mc.misTenants t " +
    	       "WHERE (:collCust IS NULL OR mc.collCust = :collCust) AND " +
    	       "(:collProperty IS NULL OR mc.collProperty = :collProperty) AND " +
    	       "(:fromDate IS NULL OR mc.collPayDate >= :fromDate) AND " +
    	       "(:toDate IS NULL OR mc.collPayDate <= :toDate) AND " +
    	       "mc.deleted = false")
    	List<MisCollection> findAllByFilters(@Param("collCust") Long collCust,
    	                                      @Param("collProperty") String collProperty,
    	                                      @Param("fromDate") LocalDate fromDate,
    	                                      @Param("toDate") LocalDate toDate);
    

    
}
