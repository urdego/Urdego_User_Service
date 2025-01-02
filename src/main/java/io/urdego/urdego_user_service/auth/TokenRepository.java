package io.urdego.urdego_user_service.auth;

import java.util.Optional;

public interface TokenRepository {
	void setRefreshToken(RefreshToken refreshToken);
	void removeRefreshToken(Long userId);
	Optional<RefreshToken> getRefreshToken(Long userId);
}
