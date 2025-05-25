package com.example.tenant_service.repository;

import com.example.tenant_service.common.BaseRepository;
import com.example.tenant_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.deleted = false")
    Optional<Event> findByIdAndNotDeleted(@Param("eventId") Long eventId);

    @Query("SELECT e FROM Event e WHERE e.deleted = false AND " +
           "(LOWER(e.eventName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Event> findAllNotDeleted(@Param("search") String search, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.hostNode.nodeId = :nodeId AND e.deleted = false")
    List<Event> findByHostNodeIdAndNotDeleted(@Param("nodeId") Long nodeId);

    @Query("SELECT e FROM Event e WHERE e.eventYear = :year AND e.deleted = false")
    List<Event> findByYearAndNotDeleted(@Param("year") Integer year);

    @Query("SELECT e FROM Event e WHERE " +
           "e.eventPeriodStart <= :date AND e.eventPeriodEnd >= :date AND e.deleted = false")
    List<Event> findActiveEventsOnDate(@Param("date") LocalDate date);

    @Query("SELECT e FROM Event e WHERE " +
           "e.eventHost = :hostType AND e.hostNode.nodeId = :hostId AND e.deleted = false")
    List<Event> findByHostTypeAndHostId(@Param("hostType") String hostType, 
                                      @Param("hostId") Long hostId);
}