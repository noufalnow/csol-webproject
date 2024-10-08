package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisPropertyPayOption;
import com.example.tenant_service.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisPropertyPayOptionRepository extends BaseRepository<MisPropertyPayOption, Long> {

    // Find all records that are not deleted with search criteria and pagination
    @Query("SELECT p FROM MisPropertyPayOption p WHERE p.deleted = false AND " +
            "(CAST(p.poptId AS string) LIKE %:search% OR " +
            "CAST(p.poptAmount AS string) LIKE %:search% OR " +
            "CAST(p.poptType AS string) LIKE %:search%)")
    Page<MisPropertyPayOption> findAllNotDeleted(@Param("search") String search, Pageable pageable);

    // Find records by poptDocId that are not deleted
    @Query("SELECT p FROM MisPropertyPayOption p WHERE p.poptDocId = :poptDocId AND p.deleted = false")
    List<MisPropertyPayOption> findByPoptDocIdAndNotDeleted(@Param("poptDocId") Long poptDocId);
    
    // New method to find records that are not deleted and not paid
    @Query("SELECT p FROM MisPropertyPayOption p WHERE p.poptDocId = :poptDocId AND p.deleted = false AND p.poptStatus = 1")
    List<MisPropertyPayOption> findByPoptDocIdAndNotDeletedAndNotPaid(@Param("poptDocId") Long poptDocId);

}
