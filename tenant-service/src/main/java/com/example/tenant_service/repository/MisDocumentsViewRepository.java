package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisDocumentsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MisDocumentsViewRepository extends JpaRepository<MisDocumentsView, Long> {

    @Query("SELECT v FROM MisDocumentsView v WHERE v.docId = :docId AND v.deleted = false")
    Optional<MisDocumentsView> findByIdAndNotDeleted(@Param("docId") Long docId);

    @Query("SELECT v FROM MisDocumentsView v WHERE "
            + "(:search IS NULL OR :search = '' OR ("
            + "LOWER(v.docNo) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.docDesc) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.propName) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(v.tenantFullName) LIKE LOWER(CONCAT('%', :search, '%')))) "
            + "AND v.deleted = false")

    Page<MisDocumentsView> searchDocuments(@Param("search") String search, Pageable pageable);
}
