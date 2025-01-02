package io.urdego.urdego_user_service.auth;

import io.urdego.urdego_user_service.auth.jwt.AuthConstants;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {
	private final RedisTemplate<String, RefreshToken> redisTemplate;

	@Override
	public void setRefreshToken(RefreshToken refreshToken) {
		redisTemplate.opsForValue().set(getKey(refreshToken.userId()) , refreshToken, AuthConstants.REFRESH_TOKEN_TTL);
	}

	@Override
	public void removeRefreshToken(Long userId) {
		redisTemplate.delete(getKey(userId));
	}

	@Override
	public Optional<RefreshToken> getRefreshToken(Long userId) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(userId)));
	}

	private String getKey(Long userId) {
		return "REFRESH_TOKEN_ER:" + userId;
	}
}
