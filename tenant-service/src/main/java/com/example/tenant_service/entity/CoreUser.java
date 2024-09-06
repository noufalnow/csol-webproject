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

    @Column(name = "user_desig")
    private Long userDesig;

    @Column(name = "user_dept")
    private Long userDept;

    @Column(name = "user_emp_id")
    private Long userEmpId;

    @Column(name = "user_email", nullable = false)
    private String userEmail;
}

