package com.dms.kalari.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import com.dms.kalari.admin.dto.validation.groups.FullValidation;
import com.dms.kalari.admin.dto.validation.groups.PartialValidation;
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
public class MemberUpdateDTO extends BaseDTO {
	
	private Long userId; 

	@NotBlank(message = "First name is required", groups = FullValidation.class)
	@Size(max = 50, message = "First name must be less than 50 characters", groups = FullValidation.class)
	private String userFname;

	@NotBlank(message = "Last name is required", groups = FullValidation.class)
	@Size(max = 50, message = "Last name must be less than 50 characters", groups = FullValidation.class)
	private String userLname;

	@NotBlank(message = "Father's name is required", groups = {FullValidation.class})
	@Size(max = 50, message = "Father's name must be less than 50 characters", groups = {FullValidation.class})
	private String fatherName;

	@NotNull(message = "Designation is required", groups = {FullValidation.class, PartialValidation.class})
	private Long userDesig;

	@NotBlank(message = "Email is required", groups = {FullValidation.class, PartialValidation.class})
	@Email(message = "Invalid email format", groups = {FullValidation.class, PartialValidation.class})
	@Size(max = 100, message = "Email must be less than 100 characters", groups = {FullValidation.class, PartialValidation.class})
	private String userEmail;

	@NotNull(message = "Date of birth is required", groups = FullValidation.class)
	@Past(message = "Date of birth must be in the past", groups = FullValidation.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate userDob;

	@NotNull(message = "Gender is required", groups = FullValidation.class)
	private Gender gender;

	@Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be 12 digits", groups = FullValidation.class)
	private String aadhaarNumber;

	@NotBlank(message = "Blood Group is required", groups = {FullValidation.class})
	private String bloodGroup;
	
    @NotNull(message = "Status is required")
    @Min(value = 1, message = "Status must be at least 1")
    @Max(value = 2, message = "Status must be at most 2")
    private Short userStatus = 1;
    
    private String motherName;


    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressState;
    
    private Long userNode;

    @Pattern(regexp = "^$|^[0-9]{6}$", message = "PIN code must be 6 digits")
    private String addressPin;

    private String emergencyContact;

    @ValidFile(maxSize = 524288, allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile photoFileId;

    @ValidFile(maxSize = 524288, allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile idFileId;

    @ValidFile(maxSize = 524288, allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile ageproofFileId;

    private Long photoFile;
    private Long idFile;
    private Long ageproofFile;

    private String filePath; 
    
    
    private DesignationDTO designation; // Reference to the designation DTO
    
    private String designationName; 
    
    
    private Short verificationStatus;
    
}
