package com.example.tenant_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import com.example.tenant_service.common.BaseDTO;
import com.example.tenant_service.entity.CoreUser;

import java.time.LocalDateTime;

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
    private LocalDateTime entryDateTime;
    private CoreUser scoreEntryBy; 
    private LocalDateTime approveDateTime;
    private CoreUser approvedBy; 
    private String uniqueId;
    
}