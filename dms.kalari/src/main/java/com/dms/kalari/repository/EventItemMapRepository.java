package com.dms.kalari.repository;

import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.EventItem;
import com.dms.kalari.entity.EventItemMap;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventItemMapRepository extends BaseRepository<EventItemMap, Long> {

    @Query("SELECT e FROM EventItemMap e WHERE e.eimId = :id AND e.deleted = false")
    Optional<EventItemMap> findByIdAndNotDeleted(Long id);

    @Query("SELECT e FROM EventItemMap e WHERE e.deleted = false")
    Page<EventItemMap> findAllNotDeleted(Pageable pageable);
    
    // Corrected method names with explicit property paths
    @Query("SELECT eim FROM EventItemMap eim WHERE eim.event.eventId = :eventId AND eim.item.evitemId = :evitemId")
    Optional<EventItemMap> findByEventIdAndEvitemId(@Param("eventId") Long eventId, @Param("evitemId") Long evitemId);


    @Query("SELECT eim FROM EventItemMap eim WHERE eim.event.eventId = :eventId")
    List<EventItemMap> findByEventId(@Param("eventId") Long eventId);
    
    // Alternative naming convention that would work without @Query
    List<EventItemMap> findByEvent_EventId(Long eventId);
    
    @Modifying
    @Query("DELETE FROM EventItemMap eim WHERE eim.event.eventId = :eventId")
    void deleteByEvent_EventId(@Param("eventId") Long eventId);
    
    @Modifying
    @Query("UPDATE EventItemMap eim SET eim.deleted = true WHERE eim.event.eventId = :eventId")
    void softDeleteByEvent_EventId(@Param("eventId") Long eventId);
    
    
    @Query("""
    	    SELECT i FROM EventItemMap m
    	    JOIN m.item i
    	    WHERE m.event.id = :eventId AND m.category = :category
    	""")
    	List<EventItem> findItemsByEventIdAndCategory(@Param("eventId") Long eventId,
    	                                              @Param("category") EventItemMap.Category category);

}