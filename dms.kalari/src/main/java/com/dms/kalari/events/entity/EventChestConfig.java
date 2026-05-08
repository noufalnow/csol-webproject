package com.dms.kalari.events.entity;

import java.time.LocalDateTime;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseEntity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "event_chest_config",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_chest_config",
            columnNames = {"ecc_eim_id", "ecc_gender"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventChestConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecc_id")
    private Long chestConfigId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ecc_eim_id", nullable = false)
    private EventItemMap eventItemMap;

    @Enumerated(EnumType.STRING)
    @Column(name = "ecc_gender", nullable = false)
    private CoreUser.Gender gender;

    @Column(name = "ecc_start_no", nullable = false)
    private Long startNo;

    @Column(name = "ecc_current_no", nullable = false)
    private Long currentNo;

    @Column(name = "ecc_prefix")
    private String prefix;

    @Column(name = "ecc_suffix")
    private String suffix;

}