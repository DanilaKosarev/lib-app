package com.example.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailUniquenessValidator.class)
public @interface ValidateEmailUniqueness {
    String message() default "This email is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
