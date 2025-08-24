package com.dms.kalari.admin.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Optional: Initialize if needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        // Implement logic if needed for other DTOs that require password matching validation
        // Return true for DTOs where validation is not required
        return true;
    }
}
