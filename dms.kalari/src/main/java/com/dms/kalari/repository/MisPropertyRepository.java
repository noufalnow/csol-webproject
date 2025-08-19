package com.dms.kalari.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.MisProperty;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisPropertyRepository extends BaseRepository<MisProperty, Long> {

    @Query("SELECT p FROM MisProperty p WHERE p.id = :propId AND p.deleted = false")
    Optional<MisProperty> findByIdAndNotDeleted(Long propId);

    @Query("SELECT p FROM MisProperty p WHERE p.deleted = false")
    Page<MisProperty> findAllNotDeleted(String search, Pageable pageable);
}
