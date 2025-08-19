package com.dms.kalari.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.MisDocuments;
import com.dms.kalari.entity.MisDocumentsView;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisDocumentsRepository extends BaseRepository<MisDocuments, Long> {

    @Query("SELECT d FROM MisDocuments d WHERE d.id = :docId AND d.deleted = false")
    Optional<MisDocuments> findByIdAndNotDeleted(Long docId);

    @Query("SELECT d FROM MisDocuments d WHERE d.deleted = false")
    Page<MisDocuments> findAllNotDeleted(String search, Pageable pageable);
    
    @Query("SELECT v FROM MisDocuments v WHERE v.deleted = false AND ((v.docAgreement = :docAgreement OR v.docId = :docAgreement) AND v.docId != :docId)")
    List<MisDocuments> findAllNotDeletedAndByDocAgreement(@Param("docAgreement") Long docAgreement, @Param("docId") Long docId);

    
    
    //@Query("SELECT d FROM Documents d WHERE d.docRefId = ?1 ORDER BY d.version DESC")
    //MisDocuments findLatestByRefId(Long docRefId);
}
