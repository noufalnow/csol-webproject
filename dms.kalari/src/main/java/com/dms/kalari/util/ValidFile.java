package com.dms.kalari.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {

    String message() default "Invalid file";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long maxSize() default 524288; // 512 KB default
    String[] allowedExtensions() default { "jpg", "jpeg", "png" };
}
