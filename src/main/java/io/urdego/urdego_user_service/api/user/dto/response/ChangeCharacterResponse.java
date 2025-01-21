package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.common.enums.CharacterType;

public record ChangeCharacterResponse(
        Long userId,
        CharacterType newCharacter
) {
    public static ChangeCharacterResponse of(Long userId, CharacterType newCharacter) {
        return new ChangeCharacterResponse(userId, newCharacter);
    }
}
