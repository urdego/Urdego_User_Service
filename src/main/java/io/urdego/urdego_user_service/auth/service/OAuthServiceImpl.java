package io.urdego.urdego_user_service.auth.service;

import io.jsonwebtoken.Claims;
import io.urdego.urdego_user_service.common.properties.KakaoOAuthProperty;
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
	private final KakaoOAuthProperty property;

	@Override
	public KakaoUserInfoDto getKakaoOAuthProfile(String code) {
		KakaoTokenDto kakaoTokenResponse = kakaoAuthFeignClient.getAccessToken("authorization_code", property.getClientId(), property.getRedirectUri(), code);
		return kakaoProfileFeignClient.getUserInfo("Bearer " + kakaoTokenResponse.getAccessToken());
	}

	@Override
	public AppleUserInfoDto getAppleOAuthProfile(String idToken) throws Exception {
		Claims claims = appleTokenVerifier.verifyIdToken(idToken);
		return AppleUserInfoDto.of(claims);
	}
}
