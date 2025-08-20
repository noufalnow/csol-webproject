package com.dms.kalari.admin.dto;

import com.dms.kalari.common.BaseDTO;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthAppMenuDTO extends BaseDTO {

    private Long appMenuId;

    @NotBlank(message = "Menu name is required")
    @Size(max = 50, message = "Menu name must be less than 50 characters")
    private String menuName;

    @NotNull(message = "Order number is required")
    private Integer orderNumber;
}
