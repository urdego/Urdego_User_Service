package io.urdego.urdego_user_service.common;

import io.urdego.urdego_user_service.auth.Tokens;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class TokenUtils {
	public static HttpHeaders createTokenHeaders(Tokens tokens) {
		HttpHeaders headers = new HttpHeaders();
		ResponseCookie cookie = createHttpOnlyCookie("refreshToken",tokens.refreshToken(),Duration.ofDays(14));
		headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
		headers.set(HttpHeaders.AUTHORIZATION, tokens.accessToken());
		return headers;
	}

	private static ResponseCookie createHttpOnlyCookie(String name, String value, Duration maxAge) {
		return ResponseCookie.from(name, value).httpOnly(true).path("/").maxAge(maxAge).build();
	}
}
