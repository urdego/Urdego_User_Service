package io.urdego.urdego_user_service.auth.service;

import io.urdego.urdego_user_service.domain.entity.dto.AppleUserInfoDto;
import io.urdego.urdego_user_service.domain.entity.dto.KakaoUserInfoDto;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.BASE_URL;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_SECRET;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.KAKAO_OAUTH_QUERY_STRING;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

public interface OAuthService {
	default String getConnectionUrl(){
		System.out.println(CLIENT_SECRET);
		return BASE_URL + String.format(KAKAO_OAUTH_QUERY_STRING,CLIENT_ID,REDIRECT_URI);
	}

	KakaoUserInfoDto getKakaoOAuthProfile(String code);


	AppleUserInfoDto getAppleOAuthProfile(String idToken) throws Exception;
}
