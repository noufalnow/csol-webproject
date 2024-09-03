package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MisProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.tenant_service.common.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisPropertyRepository extends BaseRepository<MisProperty, Long> {

    @Query("SELECT p FROM MisProperty p WHERE p.id = :propId AND p.deleted = false")
    Optional<MisProperty> findByIdAndNotDeleted(Long propId);

    @Query("SELECT p FROM MisProperty p WHERE p.deleted = false")
    List<MisProperty> findAllNotDeleted();
}
