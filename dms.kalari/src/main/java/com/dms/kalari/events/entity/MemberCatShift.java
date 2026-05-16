package com.dms.kalari.events.entity;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* if the participant category is mentioned here then override the calculated category for that item and mentioned event_id*/

@Entity
@Table(name = "member_cat_shift")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCatShift extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mcs_id")
    private Long memCatShiftId;
    
    /**
     * Event Id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcs_event_id", nullable = false)
    private Event event;
    
    /**
     * Member
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msc_member_id", nullable = false)
    private CoreUser memCatShifMemId;

    /**
     * User selected item
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcs_item_id", nullable = false)
    private EventItem item;

    /**
     * Original calculated category
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "mcs_org_category", nullable = false)
    private EventItemMap.Category originalCategory;
    
    /**
     * Shifted category selected by user
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "msc_shit_category", nullable = false)
    private EventItemMap.Category memCatShifCategory;

}