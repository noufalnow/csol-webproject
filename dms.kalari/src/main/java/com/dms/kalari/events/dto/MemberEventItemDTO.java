package com.dms.kalari.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.events.entity.EventItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberEventItemDTO extends BaseDTO {

    private Long meiId;                     // Matches entity primary key

    private CoreUser memberEventMember;      // Many-to-one CoreUser

    private Long memberEventId;              // ID of MemberEvent

    private EventItem evitem;                // Reference to EventItem entity

    private Long itemKey;                    // Matches entity type (Long)

    private Short itemValue;                 // Matches entity type (Short)

    private Integer score;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryDateTime;

    private CoreUser scoreEntryBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveDateTime;

    private CoreUser approvedBy;

    private String uniqueId;
}
