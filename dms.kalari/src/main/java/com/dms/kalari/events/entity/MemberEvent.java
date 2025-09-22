package com.dms.kalari.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_events")
public class MemberEvent extends BaseEntity {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memvnt_id")
    private Long meId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_member_id", nullable = false)
    private CoreUser member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_member_node_id", nullable = false)
    private Node memberNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_node_id", nullable = false)
    private Node node;

    @Column(name = "memvnt_apply_date", nullable = false)
    private LocalDateTime applyDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "memvnt_item", columnDefinition = "json")
    private Map<String, String> items;

    @Column(name = "memvnt_approved_date")
    private LocalDateTime approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_approved_by")
    private CoreUser approvedBy;

    @Column(name = "memvnt_result_date")
    private LocalDateTime resultDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_result_entry_by")
    private CoreUser resultEntryBy;

    @Column(name = "memvnt_result_app_date")
    private LocalDateTime resultApprovalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memvnt_result_app_by")
    private CoreUser resultApprovedBy;}