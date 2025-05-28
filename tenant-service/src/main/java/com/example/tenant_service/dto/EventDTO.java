package com.example.tenant_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import com.example.tenant_service.common.BaseDTO;
import com.example.tenant_service.dto.NodeDTO;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.entity.Node.Type;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDTO extends BaseDTO {

    private Long eventId;

    @NotBlank(message = "Event name is required")
    @Size(max = 100, message = "Event name must be less than 100 characters")
    private String eventName;

    //@NotNull(message = "Host type is required")
    private Node.Type eventHost;

    //@NotNull(message = "Host node is required")
    private Long eventHostId;

    private NodeDTO hostNode; // Reference to the full node DTO
    private String hostNodeName; // Convenience field for display

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer eventYear;

    @NotNull(message = "Start date is required")
    private LocalDate eventPeriodStart;

    @NotNull(message = "End date is required")
    private LocalDate eventPeriodEnd;

    @NotBlank(message = "Venue is required")
    @Size(max = 200, message = "Venue must be less than 200 characters")
    private String eventVenue;

    @NotBlank(message = "Official phone is required")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", 
             message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String eventOfficialPhone;

    // Validation for date consistency
    @AssertTrue(message = "End date must be after start date")
    private boolean isValidDateRange() {
        return eventPeriodEnd == null || eventPeriodStart == null || 
               eventPeriodEnd.isAfter(eventPeriodStart) || 
               eventPeriodEnd.isEqual(eventPeriodStart);
    }

    
    
    
}