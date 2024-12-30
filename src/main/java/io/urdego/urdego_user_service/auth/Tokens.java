package io.urdego.urdego_user_service.auth;

public record Tokens(String accessToken, String refreshToken) {

	public RefreshToken getRefreshToken(Long userId){
		return RefreshToken.of(userId, refreshToken);
	}
}
