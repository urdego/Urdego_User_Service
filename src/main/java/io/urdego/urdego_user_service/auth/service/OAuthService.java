package io.urdego.urdego_user_service.auth.service;

import io.urdego.urdego_user_service.common.properties.KakaoOAuthProperty;
import io.urdego.urdego_user_service.api.apple.dto.AppleUserInfoDto;
import io.urdego.urdego_user_service.api.kakao.dto.KakaoUserInfoDto;

public interface OAuthService {
	default String getConnectionUrl(KakaoOAuthProperty property){
		System.out.println(property.getClientSecret());
		return property.getBaseUrl() +
				String.format(KakaoOAuthProperty.KAKAO_OAUTH_QUERY_STRING,
						property.getClientId(),
						property.getRedirectUrl());
	}

	KakaoUserInfoDto getKakaoOAuthProfile(String code);

	AppleUserInfoDto getAppleOAuthProfile(String idToken) throws Exception;
}
