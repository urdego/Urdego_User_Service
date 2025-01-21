package io.urdego.urdego_user_service.api.user.dto.request;

import io.urdego.urdego_user_service.common.enums.PlatformType;

public record UserSignUpRequest(
		String nickname,
		String email,
		String platformType,
		String platformId
) {

}
