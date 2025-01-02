package io.urdego.urdego_user_service.auth;

import java.lang.management.LockInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {
	private final TokenRepository tokenRepository;

	public void save(RefreshToken refreshToken) {
		tokenRepository.setRefreshToken(refreshToken);
	}

	public void removeRefreshToken(Long userId) {
		tokenRepository.removeRefreshToken(userId);
	}

	public void checkRefreshToken(Long userId) {
		Optional<RefreshToken> refreshToken = tokenRepository.getRefreshToken(userId);
		//Todo Redis에 값이 없을 경우 에러 발생, 클라 쪽 로그인창으로 리다이렉트 요청
		refreshToken.orElseThrow();
	}
}
