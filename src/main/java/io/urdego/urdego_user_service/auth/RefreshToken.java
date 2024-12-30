package io.urdego.urdego_user_service.auth;

public record RefreshToken(Long userId, String refreshToken) {

	public static RefreshToken of(Long userId, String refreshToken){
		return new RefreshToken(userId, refreshToken);
	}
}
