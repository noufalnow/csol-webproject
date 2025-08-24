package com.dms.kalari.admin.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.dms.kalari.common.BaseEntity;
import com.dms.kalari.nodes.entity.Node;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "core_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoreUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_fname")
    private String userFname;

    @Column(name = "user_lname")
    private String userLname;

    @Column(name = "user_uname", nullable = false)
    private String userUname;

    @Column(name = "user_status", nullable = false)
    private Short userStatus = 1;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @ManyToOne
    @JoinColumn(name = "user_desig", referencedColumnName = "desig_id", nullable = false) // Foreign key to MisDesignation
    private MisDesignation designation; // This handles the designation relationship

    @Column(name = "user_dept")
    private Long userDept;
    
    
    @Column(name = "user_emp_id")
    private Long userEmpId;
    
    @ManyToOne
    @JoinColumn(name = "user_node_id", referencedColumnName = "node_id")
    private Node userNode;

    @Column(name = "user_email", nullable = false ,unique = true)
    private String userEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false) 
    private UserType userType;

    @Column(name = "user_dob")
    private LocalDate userDob;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_member_category")
    private MemberCategory userMemberCategory = MemberCategory.JUNIOR;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    private Gender gender;

    @Column(name = "user_aadhaar_number", length = 14)
    private String aadhaarNumber;

    @Column(name = "user_father_name")
    private String fatherName;

    @Column(name = "user_mother_name")
    private String motherName;

    @Column(name = "user_address_line1")
    private String addressLine1;

    @Column(name = "user_address_line2")
    private String addressLine2;

    @Column(name = "user_address_line3")
    private String addressLine3;

    @Column(name = "user_address_state")
    private String addressState;

    @Column(name = "user_address_pin", length = 6)
    private String addressPin;

    @Column(name = "user_state")
    private String state;
    
    @Column(name = "user_blood_group")
    private String bloodGroup;

    @Column(name = "user_nationality")
    private String nationality;

    @Column(name = "user_mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "user_emergency_contact", length = 15)
    private String emergencyContact;
    
    
    @Column(name = "photo_file")
    private Long photoFile;
    
    public enum UserType {
        MEMBER,
        OFFICIAL
    }

    public enum MemberCategory {
        SUB_JUNIOR,
        JUNIOR,
        SENIOR
    }

    public enum Gender {
        MALE,
        FEMALE
    }
}


