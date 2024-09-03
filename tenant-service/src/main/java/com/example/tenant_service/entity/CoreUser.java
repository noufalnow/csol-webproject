package com.example.tenant_service.entity;

import com.example.tenant_service.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "core_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "First name is mandatory")
    @Column(name = "user_fname", nullable = false)
    private String userFname;

    @Column(name = "user_lname")
    private String userLname;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "user_uname", nullable = false)
    private String userUname;

    @Column(name = "user_status", nullable = false)
    private Short userStatus = 1;

    @NotBlank(message = "Password is mandatory")
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @NotNull(message = "Designation ID is mandatory")
    @Column(name = "user_desig", nullable = false)
    private Long userDesig;

    @Column(name = "user_dept")
    private Long userDept;

    @NotNull(message = "Employee ID is mandatory")
    @Column(name = "user_emp_id", nullable = false)
    private Long userEmpId;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "user_email", nullable = false)
    private String userEmail;
}
