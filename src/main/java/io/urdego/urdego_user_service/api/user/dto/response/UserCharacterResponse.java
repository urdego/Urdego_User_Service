package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserCharacterResponse(
        List<String> userCharacters
) {
    public static UserCharacterResponse from(User user) {
        return UserCharacterResponse.builder()
                .userCharacters(user.getOwnedCharacters()
                        .stream()
                        .map(userCharacter -> userCharacter.getCharacter().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
