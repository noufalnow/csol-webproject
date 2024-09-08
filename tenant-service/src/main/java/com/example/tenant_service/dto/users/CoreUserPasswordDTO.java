package com.example.tenant_service.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.tenant_service.dto.validation.PasswordMatches;


@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class CoreUserPasswordDTO {

    /*@NotBlank(message = "Old password is required")
    private String oldPassword;*/

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
