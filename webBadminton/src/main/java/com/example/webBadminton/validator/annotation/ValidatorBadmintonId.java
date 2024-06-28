package com.example.webBadminton.validator.annotation;

import com.example.webBadminton.validator.ValidBadmintonIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidBadmintonIdValidator.class)
@Documented
public @interface ValidatorBadmintonId {
    String message() default "Invalid category id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
