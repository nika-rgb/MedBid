package com.medbid.medbid.business.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AuthorityTypeConverter implements AttributeConverter<AuthorityType, String> {
    @Override
    public String convertToDatabaseColumn(AuthorityType authorityType) {
        return authorityType.name();
    }

    @Override
    public AuthorityType convertToEntityAttribute(String authorityType) {
        return AuthorityType.valueOf(authorityType);
    }
}
