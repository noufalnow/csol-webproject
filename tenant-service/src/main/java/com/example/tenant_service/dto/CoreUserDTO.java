package com.example.tenant_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.tenant_service.common.BaseDTO;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreUserDTO extends BaseDTO {
    private Long userId;
    private String userFname;
    private String userLname;
    private String userUname;
    private Short userStatus = 1;
    private String userPassword;
    private Long userDesig;
    private Long userDept;
    private Long userEmpId;
    private String userEmail;
}
