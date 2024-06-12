package com.example.webBadminton.validator;

import com.example.webBadminton.model.User;
import com.example.webBadminton.validator.annotation.ValidUserId;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user == null)
            return true;
        return user.getId() != null;
    }
}
