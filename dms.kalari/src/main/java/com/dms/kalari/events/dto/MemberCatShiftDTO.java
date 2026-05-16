package com.dms.kalari.events.dto;

import com.dms.kalari.events.entity.EventItemMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCatShiftDTO {

    private Long memCatShiftId;

    /**
     * Event
     */
    private Long eventId;

    /**
     * Member
     */
    private Long memCatShifMemId;

    /**
     * Selected item
     */
    private Long itemId;

    /**
     * Original calculated category
     */
    private EventItemMap.Category originalCategory;

    /**
     * Shifted category
     */
    private EventItemMap.Category memCatShifCategory;

}