package com.dms.kalari.events.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.Event;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberCatShift;

@Repository
public interface MemberCatShiftRepository
        extends BaseRepository<MemberCatShift, Long> {

    /**
     * Exact override lookup
     */
    @Query("""
        SELECT mcs
        FROM MemberCatShift mcs
        WHERE mcs.deleted = false
          AND mcs.event = :event
          AND mcs.memCatShifMemId = :member
          AND mcs.item = :item
          AND mcs.originalCategory = :originalCategory
    """)
    Optional<MemberCatShift> findOverride(
            @Param("event") Event event,
            @Param("member") CoreUser member,
            @Param("item") EventItem item,
            @Param("originalCategory") EventItemMap.Category originalCategory
    );

    /**
     * All shifts for member in event
     */
    @Query("""
        SELECT mcs
        FROM MemberCatShift mcs
        WHERE mcs.deleted = false
          AND mcs.event = :event
          AND mcs.memCatShifMemId = :member
    """)
    List<MemberCatShift> findByEventAndMember(
            @Param("event") Event event,
            @Param("member") CoreUser member
    );

    /**
     * All shifts for item in event
     */
    @Query("""
        SELECT mcs
        FROM MemberCatShift mcs
        WHERE mcs.deleted = false
          AND mcs.event = :event
          AND mcs.item = :item
    """)
    List<MemberCatShift> findByEventAndItem(
            @Param("event") Event event,
            @Param("item") EventItem item
    );

    /**
     * All shifts for event
     */
    @Query("""
        SELECT mcs
        FROM MemberCatShift mcs
        WHERE mcs.deleted = false
          AND mcs.event.eventId = :eventId
    """)
    List<MemberCatShift> findAllByEventId(
            @Param("eventId") Long eventId
    );
    
    
    @Query("""
	    SELECT mcs
	    FROM MemberCatShift mcs
	    WHERE mcs.deleted = false
	      AND mcs.event.eventId = :eventId
	      AND mcs.memCatShifMemId = :member
	      AND mcs.item = :item
	      AND mcs.originalCategory = :originalCategory
	""")
	Optional<MemberCatShift> findOverride(
	        @Param("eventId") Long eventId,
	        @Param("member") CoreUser member,
	        @Param("item") EventItem item,
	        @Param("originalCategory") EventItemMap.Category originalCategory
	);

}