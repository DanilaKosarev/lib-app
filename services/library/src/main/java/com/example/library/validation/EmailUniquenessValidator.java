package com.example.library.validation;

import com.example.library.services.PeopleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUniquenessValidator implements ConstraintValidator<ValidateEmailUniqueness, String> {

    private final PeopleService peopleService;

    @Autowired
    public EmailUniquenessValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !peopleService.isEmailOccupied(email);
    }
}
