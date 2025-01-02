package io.urdego.urdego_user_service.auth.jwt;

import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {
	public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
	public static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(14);
}
