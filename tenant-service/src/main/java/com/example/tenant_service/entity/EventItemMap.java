// ─── 1) Entity ─────────────────────────────────────────────────────────────────

package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "event_item_map")
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
        JUNIOR
    }
}
