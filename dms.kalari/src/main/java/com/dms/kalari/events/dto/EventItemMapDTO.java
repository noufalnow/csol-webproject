// ─── 2) DTO ───────────────────────────────────────────────────────────────────

package com.dms.kalari.events.dto;

import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.events.entity.EventItemMap;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventItemMapDTO extends BaseDTO {

    private Long eimId;

    @NotNull(message = "Event ID is required")
    private Long eventId;      // maps to Event.eventId

    @NotNull(message = "Item ID is required")
    private Long evitemId;     // maps to EventItem.evitemId

    @NotNull(message = "Category is required")
    private EventItemMap.Category category;
    
    private String eimCatId;

}
