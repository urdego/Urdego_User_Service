package io.urdego.urdego_user_service.auth;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.BASE_URL;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_SECRET;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.KAKAO_OAUTH_QUERY_STRING;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;

import io.urdego.urdego_user_service.infra.kakao.dto.KakaoUserInfoDto;

public interface OAuthService {
	default String getConnectionUrl(){
		System.out.println(CLIENT_SECRET);
		return BASE_URL + String.format(KAKAO_OAUTH_QUERY_STRING,CLIENT_ID,REDIRECT_URI,CLIENT_SECRET);
	}

	KakaoUserInfoDto getOAuthProfile(String code);
}
