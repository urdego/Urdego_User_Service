package io.urdego.urdego_user_service.auth;

import lombok.Builder;

@Builder
public record Tokens(String accessToken, String refreshToken) {

	public RefreshToken getRefreshToken(Long userId){
		return RefreshToken.of(userId, refreshToken);
	}
}
