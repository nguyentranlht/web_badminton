package com.example.webBadminton.validator;


import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.validator.annotation.ValidatorBadmintonId;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidBadmintonIdValidator implements ConstraintValidator<ValidatorBadmintonId, Badminton> {
    @Override
    public boolean isValid(Badminton badminton, ConstraintValidatorContext context) {
        return badminton != null && badminton.getId() != null;
    }
}
