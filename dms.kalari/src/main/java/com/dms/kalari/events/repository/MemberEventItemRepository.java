package com.dms.kalari.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.kalari.common.BaseRepository;
import com.dms.kalari.events.entity.MemberEventItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberEventItemRepository extends BaseRepository<MemberEventItem, Long> {

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.id = :id AND mei.deleted = false")
    Optional<MemberEventItem> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.deleted = false")
    Page<MemberEventItem> findAllNotDeleted(Pageable pageable);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.memberEvent.id = :eventId AND mei.deleted = false")
    List<MemberEventItem> findByMemberEventIdAndNotDeleted(@Param("eventId") Long eventId);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.memberEventMember.id = :memberId AND mei.deleted = false")
    List<MemberEventItem> findByMemberIdAndNotDeleted(@Param("memberId") Long memberId);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.memberEvent.id = :eventId " +
           "AND mei.memberEventMember.id = :memberId AND mei.deleted = false")
    List<MemberEventItem> findByEventIdAndMemberIdAndNotDeleted(
            @Param("eventId") Long eventId,
            @Param("memberId") Long memberId);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.entryDateTime BETWEEN :startDate AND :endDate " +
           "AND mei.deleted = false")
    List<MemberEventItem> findByEntryDateRangeAndNotDeleted(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.itemKey = :itemKey AND mei.deleted = false")
    List<MemberEventItem> findByItemKeyAndNotDeleted(@Param("itemKey") Integer itemKey);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.uniqueId = :uniqueId AND mei.deleted = false")
    Optional<MemberEventItem> findByUniqueIdAndNotDeleted(@Param("uniqueId") String uniqueId);

    @Query("SELECT mei FROM MemberEventItem mei WHERE mei.memberEvent.id = :eventId " +
           "AND mei.approveDateTime IS NULL AND mei.deleted = false")
    List<MemberEventItem> findUnapprovedItemsByEventId(@Param("eventId") Long eventId);
}