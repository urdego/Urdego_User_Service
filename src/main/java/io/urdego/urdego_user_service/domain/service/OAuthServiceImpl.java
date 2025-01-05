package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.auth.OAuthService;
import io.urdego.urdego_user_service.auth.jwt.JwtTokenProvider;
import io.urdego.urdego_user_service.auth.jwt.TokenManager;
import io.urdego.urdego_user_service.auth.jwt.Tokens;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.repository.UserRepository;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements io.urdego.urdego_user_service.domain.service.OAuthService {
	private final OAuthService oAuthClient;
	private final UserRepository userRepository;
	private final JwtTokenProvider jwrTokenProvider;

	@Override
	public String getConnectionUrl() {
		return oAuthClient.getConnectionUrl();
	}

	@Override
	public String login(String code) {
		//사용자 정보 가져오기
		KakaoUserInfoDto userInfo = oAuthClient.getOAuthProfile(code);

		System.out.println("OAuthProfile: " + userInfo);

		//닉네임 중복검사 false 시 User 저장
		if(!userRepository.existsByEmail(userInfo.getKakaoAccount().getEmail())) {
			User user = userRepository.save(User.builder()
					.email(userInfo.getKakaoAccount().getEmail())
					.nickname(userInfo.getKakaoAccount().getProfile().getNickname())
					.profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImage())
					.build());

			//JWT 생성
			return jwrTokenProvider.generateToken(user.getEmail());
		}
		return null;
	}
}
