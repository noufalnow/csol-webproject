package com.dms.kalari.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.entity.Event;
import com.dms.kalari.entity.EventItemMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;



@Repository
public interface EventRepository extends BaseRepository<Event, Long> {

    JdbcTemplate jdbcTemplate = null;

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
    
    
    @Query("""
    	    SELECT DISTINCT e FROM Event e
    	    JOIN FETCH e.eventItemMaps m
    	    JOIN FETCH m.item i
    	    WHERE e.id = :eventId AND e.deleted = false AND m.category = :category
    	""")
    	Optional<Event> findByIdWithItemsByCategory(@Param("eventId") Long eventId,
    	                                            @Param("category") EventItemMap.Category category);


    
    
    /*@Query(value = """
            WITH RECURSIVE node_hierarchy AS (
                SELECT node_id, parent_id
                FROM nodes
                WHERE node_id = :nodeId
                UNION ALL
                SELECT n.node_id, n.parent_id
                FROM nodes n
                INNER JOIN node_hierarchy nh ON n.node_id = nh.parent_id
                WHERE n.deleted = false
            )
            SELECT e.*, me.*
            FROM events e
            LEFT JOIN member_events me ON me.memvnt_event_id = e.event_id 
                AND me.memvnt_member_id = :memberId 
                AND me.deleted = false
            JOIN node_hierarchy nh ON e.event_host_id = nh.node_id
            WHERE e.deleted = false
            """, nativeQuery = true)
        List<Object[]> findEventsByMemberAndNodeHierarchy(@Param("nodeId") Long nodeId, @Param("memberId") Long memberId);*/
    
    
}