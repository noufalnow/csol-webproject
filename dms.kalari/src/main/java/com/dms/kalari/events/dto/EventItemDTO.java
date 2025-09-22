package com.dms.kalari.events.dto;

import com.dms.kalari.common.BaseDTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventItemDTO extends BaseDTO {

    private Long evitemId;

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must be less than 100 characters")
    private String evitemName;

    @NotBlank(message = "Item code is required")
    @Size(max = 50, message = "Item code must be less than 50 characters")
    private String evitemCode;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String evitemDescription;

    @Size(max = 200, message = "Criteria must be less than 200 characters")
    private String evitemCriteria;
}
