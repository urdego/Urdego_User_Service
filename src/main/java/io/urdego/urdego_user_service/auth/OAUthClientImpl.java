package io.urdego.urdego_user_service.auth;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

import io.urdego.urdego_user_service.api.kakao.dto.KakaoLoginRequest;
import io.urdego.urdego_user_service.infra.kakao.KakaoAuthFeignClient;
import io.urdego.urdego_user_service.infra.kakao.KakaoProfileFeignClient;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAUthClientImpl implements OAuthClient{
	private final KakaoAuthFeignClient kakaoAuthFeignClient;
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public OAuthProfile getOAuthProfile(KakaoLoginRequest request) {
		KakaoTokenResponse kakaoTokenResponse = kakaoAuthFeignClient.auth(CLIENT_ID, REDIRECT_URI, request.accessToken());
		return kakaoProfileFeignClient.getUserInfo("bearer" + kakaoTokenResponse.access_token());
	}
}
