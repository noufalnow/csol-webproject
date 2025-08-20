package com.dms.kalari.admin.dto;

import com.dms.kalari.common.BaseDTO;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthAppPageDTO extends BaseDTO {

    private Long appPageId;

    @NotNull(message = "Menu unique ID is required")
    private Long menuUniqueId;

    private AuthAppMenuDTO menu; // Optional nested reference

    @NotBlank(message = "Page name is required")
    @Size(max = 50, message = "Page name must be less than 50 characters")
    private String pageName;

}
