package com.dms.kalari.events.repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.MemberEventItem;

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

}
