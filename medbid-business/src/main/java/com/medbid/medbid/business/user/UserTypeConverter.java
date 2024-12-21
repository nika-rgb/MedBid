package com.medbid.medbid.business.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType userType) {
        return userType.name();
    }

    @Override
    public UserType convertToEntityAttribute(String userType) {
        return UserType.valueOf(userType);
    }
}
