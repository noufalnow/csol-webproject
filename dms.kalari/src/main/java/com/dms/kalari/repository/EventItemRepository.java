package com.dms.kalari.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.EventItem;

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
