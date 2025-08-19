package com.dms.kalari.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.common.BaseDTO;
import com.dms.kalari.dto.validation.PasswordMatches;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PasswordMatches
public class UserMemberDTO extends BaseDTO {

    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String userFname;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String userLname;

    @NotNull(message = "Status is required")
    @Min(value = 1, message = "Status must be at least 1")
    @Max(value = 2, message = "Status must be at most 2")
    private Short userStatus = 1;
    
    @NotNull(message = "Designation is required")
    private Long userDesig;  // Designation ID
    
    private DesignationDTO designation; // Reference to the designation DTO
    
    private String designationName; 
    
    private Long userNode;
    
    private String userUname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String userEmail;
    private UserType userType = UserType.MEMBER;
    private String userPassword;
}
