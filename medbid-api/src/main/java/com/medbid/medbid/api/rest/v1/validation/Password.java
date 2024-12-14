package com.medbid.medbid.api.rest.v1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default """
            Valid password should have at least one lowercase and uppercase letter,
            "one number, one symbol and length should be more than 8
    """;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
