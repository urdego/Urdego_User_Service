package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.kakao.dto.KakaoLoginRequest;
import io.urdego.urdego_user_service.auth.OAuthProfile;
import io.urdego.urdego_user_service.auth.RefreshToken;
import io.urdego.urdego_user_service.auth.TokenManager;
import io.urdego.urdego_user_service.auth.Tokens;
import io.urdego.urdego_user_service.auth.UserPrincipal;
import io.urdego.urdego_user_service.auth.jwt.JwrTokenProvider;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.repository.UserRepository;
import io.urdego.urdego_user_service.auth.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
	private final OAuthClient oAuthClient;
	private final UserRepository userRepository;
	private final JwrTokenProvider jwrTokenProvider;
	private final TokenManager tokenManager;

	@Override
	public String getConnectionUrl() {
		return oAuthClient.getConnectionUrl();
	}

	@Override
	public Tokens login(KakaoLoginRequest request) {
		OAuthProfile oAuthProfile = oAuthClient.getOAuthProfile(request);
		//닉네임 중복검사 false 시 User 저장
		if(!userRepository.existsByNickname(oAuthProfile.getNickname())) {
			userRepository.save(User.builder()
							.nickname(oAuthProfile.getNickname())
					.build());
		}
		//TODO 예외처리 해야 됨
		User user = userRepository.findByNickname(oAuthProfile.getNickname()).orElseThrow();
		Tokens tokens = jwrTokenProvider.generate(UserPrincipal.of(user.getId(),user.getRole()));
		tokenManager.save(RefreshToken.of(user.getId(), tokens.refreshToken()));
		return tokens;
	}
}
