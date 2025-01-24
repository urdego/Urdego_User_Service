package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.common.enums.CharacterType;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.domain.entity.User;
import lombok.Builder;
@Builder
public record UserResponse(
		Long userId,
		String email,
		String nickname,
		String platformId,
		PlatformType platformType,
		CharacterType characterType,
		Long exp,
		Role role
) {
	public static UserResponse from(User user) {
		return new UserResponse(user.getId(),
				user.getEmail(),
				user.getNickname(),
				user.getPlatformId(),
				user.getPlatformType(),
				user.getCharacterType(),
				user.getExp(),
				user.getRole());
	}
}
