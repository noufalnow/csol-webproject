package com.dms.kalari.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dms.kalari.common.BaseEntity;
import com.dms.kalari.nodes.entity.Node;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_host", nullable = false)
    private Type eventHost;

    @ManyToOne
    @JoinColumn(name = "event_host_id", referencedColumnName = "node_id", nullable = false)
    private Node hostNode;

    @Column(name = "event_year", nullable = false)
    private Integer eventYear;

    @Column(name = "event_period_start", nullable = false)
    private LocalDate eventPeriodStart;

    @Column(name = "event_period_end", nullable = false)
    private LocalDate eventPeriodEnd;

    @Column(name = "event_venue", nullable = false)
    private String eventVenue;

    @Column(name = "event_official_phone", nullable = false)
    private String eventOfficialPhone;

    public enum Type {
        ROOT,
        COUNTRY,
        STATE,
        DISTRICT,
        KALARI
    }
    
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventItemMap> eventItemMaps;


    public List<EventItemMap> getEventItemMaps() {
        return eventItemMaps;
    }


}