package com.example.tenant_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import com.example.tenant_service.common.BaseDTO;
import com.example.tenant_service.dto.validation.PasswordMatches;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PasswordMatches
public class CoreUserDTO extends BaseDTO {

    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String userFname;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String userLname;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String userUname;

    @NotNull(message = "Status is required")
    @Min(value = 1, message = "Status must be at least 1")
    @Max(value = 2, message = "Status must be at most 2")
    private Short userStatus = 1;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String userPassword;

    @NotBlank(message = "Password confirmation is required")
    private String userPasswordConf;

    private Long userDesig;
    private Long userDept;

    @NotNull(message = "Employee ID is required")
    private Long userEmpId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String userEmail;


}