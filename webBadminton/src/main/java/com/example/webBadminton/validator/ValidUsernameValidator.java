package com.example.webBadminton.validator;


import com.example.webBadminton.repository.IUserRepository;
import com.example.webBadminton.validator.annotation.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userRepository == null) {
            return true; // Null username is considered invalid
        }
        // Return true if the username is not found in the database, indicating it is valid (i.e., unique)
        return userRepository.findByUsername(username) == null;
    }
}

