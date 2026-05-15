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
     * User selected member
     */
    private Long memCatShifMemId;

    /**
     * User selected item
     */
    private Long itemId;

    /**
     * Calculated original category
     */
    private EventItemMap.Category originalCategory;

    /**
     * User selected shifted category
     */
    private EventItemMap.Category memCatShifCategory;

    /**
     * Derived original EIM
     */
    private Long memCatShifOrgEimId;

    /**
     * Derived shifted EIM
     */
    private Long memCatShifEimId;
}