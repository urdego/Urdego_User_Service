package io.urdego.urdego_user_service.infra.kakao;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

import io.urdego.urdego_user_service.auth.OAuthProfile;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAUthClientImpl implements OAuthClient{
	private final KakaoAuthFeignClient kakaoAuthFeignClient;
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public OAuthProfile getOAuthProfile(String code) {
		KakaoTokenResponse kakaoTokenResponse = kakaoAuthFeignClient.auth(CLIENT_ID, REDIRECT_URI, code);
		return kakaoProfileFeignClient.getUserInfo("bearer" + kakaoTokenResponse.access_token());
	}
}
