package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_events_items")
public class MemberEventItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mei_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_member_id", nullable = false)
    private CoreUser memberEventMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_memvnt_id", nullable = false)
    private MemberEvent memberEvent;

    @Column(name = "mei_item_key", nullable = false)
    private Integer itemKey;  // Corresponds to your Map<Integer, String> key

    @Column(name = "mei_item_value", nullable = false)
    private String itemValue;  // The string value

    @Column(name = "mei_score")
    private Integer score;

    @Column(name = "mei_entry_datetime", nullable = false)
    private LocalDateTime entryDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_entry_by", nullable = false)
    private CoreUser scoreEntryBy;

    @Column(name = "mei_approve_datetime")
    private LocalDateTime approveDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_approve_by")
    private CoreUser approvedBy;

    @Column(name = "mei_unique_id", unique = true)
    private String uniqueId;
}