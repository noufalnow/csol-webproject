package com.dms.kalari.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreUserToggleDTO {

    @NotNull(message = "Reference ID is required")
    private Long userId;

    @NotNull(message = "Status is required")
    @Min(value = 1, message = "Status must be at least 1")
    @Max(value = 2, message = "Status must be at most 2")
    private Short userStatus = 1;
}

