package com.dms.kalari.events.repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.MemberEventItem;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberEventItemRepository extends BaseRepository<MemberEventItem, Long> {

    /**
     * Fetch a single MemberEventItem by id, ensuring it is not soft-deleted.
     */
    @Query("SELECT m FROM MemberEventItem m " +
           "WHERE m.meiId = :id AND m.deleted = false")
    Optional<MemberEventItem> findByIdAndNotDeleted(@Param("id") Long id);
    
    
    @Query("SELECT m FROM MemberEventItem m " +
            "WHERE m.memberEvent.eventId = :eventId " +
            "AND m.memberEventNode.nodeId = :nodeId " +
            "AND m.deleted = false")
     List<MemberEventItem> findByEventAndNode(@Param("eventId") Long eventId,
                                              @Param("nodeId") Long nodeId);
    
    
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM MemberEventItem m
        WHERE m.memberEventMap.eimId = :eventItemId
          AND m.memberEventMember.userId = :userId
          AND m.memberEventMap.event.id = :eventId
          AND m.memberEventNode.id = :nodeId
    """)
//@todo  add condition grade and score is empty
    
    void deleteByEventItemAndUserAndEventAndNode(@Param("eventItemId") Long eventItemId,
                                                 @Param("userId") Long userId,
                                                 @Param("eventId") Long eventId,
                                                 @Param("nodeId") Long nodeId);
    
    
    @Query("""
    	    SELECT mei
    	    FROM MemberEventItem mei
    	    JOIN FETCH mei.memberEventItem ei
    	    JOIN FETCH mei.memberEventMember cu
    	    JOIN FETCH mei.memberEventNode n
    	    WHERE mei.memberEvent.eventId = :eventId
    	    ORDER BY
    	        ei.evitemName ASC,
    	        mei.memberEventGender ASC,
    	        mei.memberEventCategory ASC,
    	        n.nodeName ASC,
    	        cu.userFname ASC
    	""")
    	List<MemberEventItem> findByEventId(@Param("eventId") Long eventId);



}
