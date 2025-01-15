/*
package io.urdego.urdego_user_service.auth.service.kakao;

import io.urdego.urdego_user_service.auth.jwt.JwtService;
import io.urdego.urdego_user_service.auth.jwt.TokenRes;
import io.urdego.urdego_user_service.auth.service.OAuthService;
import io.urdego.urdego_user_service.common.properties.KakaoOAuthProperty;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.api.kakao.dto.KakaoUserInfoDto;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {
	private final OAuthService oAuthService;
	private final UserRepository userRepository;
	private final KakaoOAuthProperty property;
	private final JwtService jwtService;

	@Override
	public String getConnectionUrl() {
		return oAuthService.getConnectionUrl(property);
	}

	@Override
	public TokenRes kakaoLogin(String code) {
		//사용자 정보 가져오기
		KakaoUserInfoDto userInfo = oAuthService.getKakaoOAuthProfile(code);

		System.out.println("userInfo PlatformId: " + userInfo.getId());
		System.out.println("userInfo email : " + userInfo.getKakaoAccount().getEmail() );
		System.out.println("userInfo nickname : " + userInfo.getKakaoAccount().getProfile().getNickname());

		//닉네임 중복검사 false 시 User 저장
		//TODO 동일한 이메일로 이미 가입되어 있다면..?
		if(!userRepository.existsByEmail(userInfo.getKakaoAccount().getEmail())) {
			User user = User.createKakaoUser(userInfo);
			userRepository.save(user);

			//JWT 생성
			return jwtService.createJwt(userInfo.getId().toString());
		}
		return jwtService.createJwt(userInfo.getId().toString());
	}
}
*/
