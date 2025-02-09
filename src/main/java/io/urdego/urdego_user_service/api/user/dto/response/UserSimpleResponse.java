package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.domain.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public record UserSimpleResponse(
        Long userId,
        String nickname,
        String activeCharacter,
        List<String> ownedCharacters,
        Long exp
) {
    public static UserSimpleResponse from(User user) {
        return new UserSimpleResponse(
                user.getId(),
                user.getNickname(),
                user.getActiveCharacter().getName() == null ? null : user.getActiveCharacter().getName(),
                user.getOwnedCharacters().stream()
                        .map(userCharacter -> userCharacter.getCharacter().getName())
                        .collect(Collectors.toList()),
                user.getExp()
        );
    }
}
