package com.dms.kalari.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.events.entity.EventItem;
import com.dms.kalari.events.entity.EventItemMap;
import com.dms.kalari.events.entity.MemberEventItem.Grade;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberEventItemDTO extends BaseDTO {

    private Long meiId;                         // Matches entity primary key

    private CoreUser memberEventMember;         // Many-to-one CoreUser

    private EventItem memberEventItem;          // Reference to EventItem entity

    private Node memberEventNode;               // Node of the member

    private Node memberEventHost;               // Node of the event host

    private EventItemMap.Category memberEventCategory; // Enum category

    private CoreUser.Gender memberEventGender;  // Enum gender

    private String memberEventItemName;         // Item name

    private Integer memberEventScore;           // Score

    private Grade memberEventGrade;          // Grade

    private CoreUser scoreEntryBy;              // User who entered score

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveDateTime;      // Approval datetime

    private CoreUser approvedBy;                // User who approved
}
