package io.urdego.urdego_user_service.auth;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

import io.urdego.urdego_user_service.infra.kakao.KakaoAuthFeignClient;
import io.urdego.urdego_user_service.infra.kakao.KakaoProfileFeignClient;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoResourceDto;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenDto;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAUthServiceImpl implements OAuthService {
	private final KakaoAuthFeignClient kakaoAuthFeignClient;
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public KakaoUserInfoDto getOAuthProfile(String code) {
		KakaoTokenDto kakaoTokenResponse = kakaoAuthFeignClient.getAccessToken("authorization_code",CLIENT_ID,REDIRECT_URI,code);
		return kakaoProfileFeignClient.getUserInfo("Bearer " + kakaoTokenResponse.getAccessToken());
	}
}
