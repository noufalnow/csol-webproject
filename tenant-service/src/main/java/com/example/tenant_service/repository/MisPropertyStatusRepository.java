package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisPropertyStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.tenant_service.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisPropertyStatusRepository extends BaseRepository<MisPropertyStatus, Long> {

    @Query("SELECT ps FROM MisPropertyStatus ps WHERE ps.id = :pstsId AND ps.deleted = false")
    Optional<MisPropertyStatus> findByIdAndNotDeleted(Long pstsId);

    @Query("SELECT ps FROM MisPropertyStatus ps WHERE ps.deleted = false")
    Page<MisPropertyStatus> findAllNotDeleted(String search, Pageable pageable);
}
