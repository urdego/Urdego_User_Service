/*
package io.urdego.urdego_user_service.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.urdego.urdego_user_service.common.enums.CharacterType;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class CharacterListConverter implements AttributeConverter<List<CharacterType>,String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<CharacterType> characterTypes) {
        try{
            return objectMapper.writeValueAsString(characterTypes);
        }
    }

    @Override
    public List<CharacterType> convertToEntityAttribute(String s) {
        return List.of();
    }
}
*/
