package com.dms.kalari.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.entity.CoreUser.MemberCategory;
import com.dms.kalari.admin.dto.validation.PasswordMatches;
import com.dms.kalari.admin.entity.CoreUser.Gender;
import com.dms.kalari.common.BaseDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import com.dms.kalari.util.ValidFile;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PasswordMatches
public class OfficialAddDTO extends BaseDTO {

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
    
    // New fields
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate userDob;


    @NotNull(message = "Gender is required")
    private Gender gender;
    
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be 12 digits")
    private String aadhaarNumber;
    
    @NotBlank(message = "Blood Group is required")
    private String bloodGroup;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressState;
    
    @Pattern(regexp = "^$|^[0-9]{6}$", message = "PIN code must be 6 digits")
    private String addressPin;

    
    private String emergencyContact;
    
    @ValidFile(maxSize = 524288, allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile photoFileId;
    
    private String filePath;  
    
    private Long photoFile;
    
}