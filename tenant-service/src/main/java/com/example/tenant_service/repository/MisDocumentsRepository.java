package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisDocuments;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.tenant_service.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisDocumentsRepository extends BaseRepository<MisDocuments, Long> {

    @Query("SELECT d FROM MisDocuments d WHERE d.id = :docId AND d.deleted = false")
    Optional<MisDocuments> findByIdAndNotDeleted(Long docId);

    @Query("SELECT d FROM MisDocuments d WHERE d.deleted = false")
    List<MisDocuments> findAllNotDeleted();
}
