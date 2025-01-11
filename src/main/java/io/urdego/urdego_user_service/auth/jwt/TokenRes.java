package io.urdego.urdego_user_service.auth.jwt;

public record TokenRes(
		String accessToken,
		String refreshToken
) {

	public static TokenRes of(String accessToken, String refreshToken) {
		return new TokenRes(accessToken, refreshToken);
	}
}
