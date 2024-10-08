package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisDocumentsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisDocumentsViewRepository extends JpaRepository<MisDocumentsView, Long> {

    @Query("SELECT v FROM MisDocumentsView v WHERE v.docId = :docId AND v.deleted = false")
    Optional<MisDocumentsView> findByIdAndNotDeleted(@Param("docId") Long docId);
    
    
    @Query("SELECT v FROM MisDocumentsView v WHERE v.docTntId = :docTntId AND v.deleted = false")
    List<MisDocumentsView> findByDocTntIdAndNotDeleted(@Param("docTntId") Long docTntId);
    
    // New method to find documents with pending payment options
    @Query("SELECT v FROM MisDocumentsView v WHERE v.docTntId = :docTntId AND v.deleted = false " +
           "AND EXISTS (SELECT 1 FROM MisPropertyPayOption p WHERE p.poptDocId = v.docId AND p.poptStatus = 1)")
    List<MisDocumentsView> findByDocTntIdAndPendingPayments(@Param("docTntId") Long docTntId);
    
    

    @Query("SELECT v FROM MisDocumentsView v WHERE v.deleted = false")
    List<MisDocumentsView> findAllNotDeleted();
    

    @Query("SELECT v FROM MisDocumentsView v WHERE "
            + "(:search IS NULL OR :search = '' OR ("
            + "LOWER(v.docNo) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.docDesc) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.propName) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.tenantFullName) LIKE LOWER(CONCAT('%', :search, '%')))) "
            + "AND v.deleted = false")
    Page<MisDocumentsView> searchDocuments(@Param("search") String search, Pageable pageable);
}
