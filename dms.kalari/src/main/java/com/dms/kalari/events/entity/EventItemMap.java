package com.dms.kalari.events.entity;

import com.dms.kalari.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(
	    name = "event_item_map",
	    uniqueConstraints = @UniqueConstraint(
	        name = "uq_event_item_map_event_cat_item",
	        columnNames = {"eim_event_id", "eim_cat_id", "eim_item_id"}
	    )
	)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItemMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eim_id")
    private Long eimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eim_event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eim_item_id", referencedColumnName = "evitem_id", nullable = false)
    private EventItem item;

    @Enumerated(EnumType.STRING)
    @Column(name = "eim_cat_id", nullable = false)
    private Category category;

    public enum Category {
        SENIOR,
        JUNIOR,
        SUBJUNIOR
    }
}
