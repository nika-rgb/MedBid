package com.medbid.medbid.api.rest.v1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

    private Set<String> enumValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        enumValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());

    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || enumValues.contains(value);
    }

}
