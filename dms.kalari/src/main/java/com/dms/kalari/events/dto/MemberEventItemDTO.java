package com.dms.kalari.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberEventItemDTO extends BaseDTO {
    private Long id;
    
    private CoreUser memberEventMember;
    private Long memberEventId;   // Reference to MemberEvent
    private Integer itemKey;     // The Integer key from your original Map
    private String itemValue;    // The String value
    private Integer score;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime entryDateTime;
    private CoreUser scoreEntryBy; 
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime approveDateTime;
    private CoreUser approvedBy; 
    private String uniqueId;
    
}