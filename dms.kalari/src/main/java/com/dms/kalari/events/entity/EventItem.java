package com.dms.kalari.events.entity;

import com.dms.kalari.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "event_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evitem_id")
    private Long evitemId;

    @Column(name = "evitem_name", nullable = false)
    private String evitemName;

    @Column(name = "evitem_code", nullable = false, unique = true)
    private String evitemCode;

    @Column(name = "evitem_description")
    private String evitemDescription;

    @Column(name = "evitem_criteria")
    private String evitemCriteria;
}
