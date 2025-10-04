package com.dms.kalari.events.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
	    name = "member_events_items",
	    uniqueConstraints = @UniqueConstraint(
	        name = "uq_member_events_items_event_member_item",
	        columnNames = {"mei_event_id", "mei_member_id", "mei_item_id"}
	    )
	)
public class MemberEventItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mei_id")
    private Long meiId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_event_id", nullable = false)
    private Event memberEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_member_node", nullable = false)
    private Node memberEventNode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_member_id", nullable = false)
    private CoreUser memberEventMember;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_item_id", nullable = false)
    private EventItem memberEventItem;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_eim_id", nullable = false)
    private EventItemMap memberEventMap;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_event_host", nullable = false)
    private Node memberEventHost;

    @Enumerated(EnumType.STRING)
    @Column(name = "mei_category", nullable = false)
    private EventItemMap.Category memberEventCategory;
    
    @Column(name = "mei_gender")
    @Enumerated(EnumType.STRING) 
    private CoreUser.Gender memberEventGender;
    
    @Column(name = "mei_item_name")
    private String memberEventItemName;
    
    @Column(name = "mei_score")
    private Integer memberEventScore;
    
    @Column(name = "mei_grade")
    @Enumerated(EnumType.STRING)     // store the enum name in DB
    private Grade memberEventGrade;

    
    
    public enum Grade {
        GOLD,
        SILVER,
        BRONZE,
        PARTICIPATION
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_entry_by", nullable = true)
    private CoreUser scoreEntryBy;

    @Column(name = "mei_approve_datetime", nullable = true)
    private LocalDateTime approveDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mei_approve_by", nullable = true)
    private CoreUser approvedBy;

}
