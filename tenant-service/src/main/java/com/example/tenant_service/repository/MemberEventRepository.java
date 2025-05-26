package com.example.tenant_service.repository;

import com.example.tenant_service.entity.MemberEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.member.userId = :memberId AND me.deleted = false")
    List<MemberEvent> findByMemberByUserId(Long memberId);

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.node.nodeId = :nodeId AND me.deleted = false")
    List<MemberEvent> findByNodeByNodeId(Long nodeId);

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.event.eventId = :eventId AND me.deleted = false")
    List<MemberEvent> findByEventByEventId(Long eventId);

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.approvedBy IS NULL AND me.deleted = false")
    List<MemberEvent> findByApprovedByIsNull();

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.resultApprovedBy IS NULL AND me.resultEntryBy IS NOT NULL AND me.deleted = false")
    List<MemberEvent> findByResultApprovedByIsNullAndResultEntryByIsNotNull();

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.approvedDate IS NULL AND me.deleted = false")
    List<MemberEvent> findPendingApprovals();

    // Original method with added soft-delete check
    @Query("SELECT me FROM MemberEvent me WHERE me.resultDate IS NOT NULL AND me.resultApprovalDate IS NULL AND me.deleted = false")
    List<MemberEvent> findPendingResultApprovals();
}
