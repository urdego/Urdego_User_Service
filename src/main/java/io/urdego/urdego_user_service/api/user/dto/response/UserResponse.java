package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.common.enums.CharacterType;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserResponse(
		Long userId,
		String email,
		String name,
		String nickname,
		String platformId,
		PlatformType platformType,
		String activeCharacter,
		List<String> ownedCharacters,
		Long exp,
		Role role
) {
	public static UserResponse from(User user) {
		return new UserResponse(user.getId(),
				user.getEmail(),
				user.getName(),
				user.getNickname(),
				user.getPlatformId(),
				user.getPlatformType(),
				user.getActiveCharacter().getName() == null ? null : user.getActiveCharacter().getName(),
				user.getOwnedCharacters().stream()
						.map(userCharacter -> userCharacter.getCharacter().getName())
						.collect(Collectors.toList()),
				user.getExp(),
				user.getRole());
	}
}
