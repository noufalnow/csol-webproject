package com.example.tenant_service.dto.validation;

import com.example.tenant_service.dto.CoreUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, CoreUserDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(CoreUserDTO dto, ConstraintValidatorContext context) {
        if (dto.getUserPassword() == null || dto.getUserPasswordConf() == null) {
            return true; // Assume that if passwords are not provided, they match.
        }
        return dto.getUserPassword().equals(dto.getUserPasswordConf());
    }
}

