package io.urdego.urdego_user_service.auth.service;

import io.urdego.urdego_user_service.auth.jwt.JwtTokenProvider;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.dto.KakaoUserInfoDto;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {
	private final OAuthService oAuthService;
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public String getConnectionUrl() {
		return oAuthService.getConnectionUrl();
	}

	@Override
	public String login(String code) {
		//사용자 정보 가져오기
		KakaoUserInfoDto userInfo = oAuthService.getOAuthProfile(code);

		System.out.println("OAuthProfile: " + userInfo.getId());

		//닉네임 중복검사 false 시 User 저장
		if(!userRepository.existsByEmail(userInfo.getKakaoAccount().getEmail())) {
			User user = User.createKakaoUser(userInfo);

			//JWT 생성
			return jwtTokenProvider.generateToken(user.getEmail());
		}
		return jwtTokenProvider.generateToken(userInfo.getKakaoAccount().getEmail());
	}
}
