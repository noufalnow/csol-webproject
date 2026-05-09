package com.dms.kalari.events.dto;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.events.entity.EventItemMap;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventChestConfigDTO extends BaseDTO {

    private Long chestConfigId;

    @NotNull(message = "Event item map id is required")
    private Long eventItemMapId;

    private Long eventId;

    private Long itemId;

    private String itemName;

    private EventItemMap.Category category;

    @NotNull(message = "Gender is required")
    private CoreUser.Gender gender;

    @NotNull(message = "Start number is required")
    private Long startNo;

    @NotNull(message = "Current number is required")
    private Long currentNo;

    @Size(max = 20)
    private String prefix;

    @Size(max = 20)
    private String suffix;
    
    @Size(max = 100)
    private String judge1;
    
    @Size(max = 100)
    private String judge2;
    
    @Size(max = 100)
    private String judge3;
    

    public EventChestConfigDTO(
            Long chestConfigId,
            Long eventItemMapId,
            Long eventId,
            Long itemId,
            String itemName,
            EventItemMap.Category category,
            CoreUser.Gender gender,
            Long startNo,
            Long currentNo,
            String prefix,
            String suffix
    ) {
        this.chestConfigId = chestConfigId;
        this.eventItemMapId = eventItemMapId;
        this.eventId = eventId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.gender = gender;
        this.startNo = startNo;
        this.currentNo = currentNo;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}