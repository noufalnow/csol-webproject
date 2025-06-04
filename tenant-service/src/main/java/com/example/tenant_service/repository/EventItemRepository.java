package com.example.tenant_service.repository;

import com.example.tenant_service.entity.EventItem;
import com.example.tenant_service.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface EventItemRepository extends BaseRepository<EventItem, Long> {

    @Query("SELECT ei FROM EventItem ei WHERE ei.evitemId = :id AND ei.deleted = false")
    Optional<EventItem> findByIdAndNotDeleted(Long id);

    @Query("SELECT ei FROM EventItem ei WHERE ei.deleted = false")
    Page<EventItem> findAllNotDeleted(String search, Pageable pageable);    
}
