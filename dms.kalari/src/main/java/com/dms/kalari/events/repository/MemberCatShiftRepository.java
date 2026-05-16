package com.dms.kalari.events.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberCatShift;

@Repository
public interface MemberCatShiftRepository
        extends BaseRepository<MemberCatShift, Long> {

    /**
     * Find override category shift for a member + event item map
     */
    @Query("""
            SELECT mcs
            FROM MemberCatShift mcs
            WHERE mcs.deleted = false
              AND mcs.memCatShifMemId = :member
              AND mcs.memCatShifEim = :eventItemMap
           """)
    Optional<MemberCatShift> findByMemCatShifMemIdAndMemCatShifEim(
            @Param("member") CoreUser member,
            @Param("eventItemMap") EventItemMap eventItemMap
    );
    
    
    @Query("""
	        SELECT mcs
	        FROM MemberCatShift mcs
	        WHERE mcs.deleted = false
	          AND mcs.memCatShifMemId = :member
	          AND mcs.memCatShifOrgEim = :eventItemMap
	       """)
	Optional<MemberCatShift> findByMemCatShifMemIdAndMemCatShifOrgEim(
	        @Param("member") CoreUser member,
	        @Param("eventItemMap") EventItemMap eventItemMap
	);

    /**
     * Find all shifts for a member
     */
    @Query("""
            SELECT mcs
            FROM MemberCatShift mcs
            WHERE mcs.deleted = false
              AND mcs.memCatShifMemId = :member
           """)
    List<MemberCatShift> findByMember(
            @Param("member") CoreUser member
    );

    /**
     * Find all shifts for an event item map
     */
    @Query("""
            SELECT mcs
            FROM MemberCatShift mcs
            WHERE mcs.deleted = false
              AND mcs.memCatShifEim = :eventItemMap
           """)
    List<MemberCatShift> findByEventItemMap(
            @Param("eventItemMap") EventItemMap eventItemMap
    );
    
    
    @Query("""
	    SELECT mcs
	    FROM MemberCatShift mcs
	    WHERE mcs.deleted = false
	      AND mcs.memCatShifEim.event.eventId = :eventId
	""")
	List<MemberCatShift> findAllByEventId(
	        @Param("eventId") Long eventId
	);

}