package io.urdego.urdego_user_service.auth.service;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

import io.jsonwebtoken.Claims;
import io.urdego.urdego_user_service.domain.entity.dto.AppleUserInfoDto;
import io.urdego.urdego_user_service.domain.entity.dto.KakaoUserInfoDto;
import io.urdego.urdego_user_service.infra.apple.AppleTokenVerifier;
import io.urdego.urdego_user_service.infra.kakao.KakaoAuthFeignClient;
import io.urdego.urdego_user_service.infra.kakao.KakaoProfileFeignClient;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
	private final KakaoAuthFeignClient kakaoAuthFeignClient;
	private final KakaoProfileFeignClient kakaoProfileFeignClient;
	private final AppleTokenVerifier appleTokenVerifier;

	@Override
	public KakaoUserInfoDto getKakaoOAuthProfile(String code) {
		KakaoTokenDto kakaoTokenResponse = kakaoAuthFeignClient.getAccessToken("authorization_code",CLIENT_ID,REDIRECT_URI,code);
		return kakaoProfileFeignClient.getUserInfo("Bearer " + kakaoTokenResponse.getAccessToken());
	}

	@Override
	public AppleUserInfoDto getAppleOAuthProfile(String idToken) throws Exception {
		Claims claims = appleTokenVerifier.verifyIdToken(idToken);
		return AppleUserInfoDto.of(claims);
	}
}
