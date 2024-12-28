package io.urdego.urdego_user_service.infra.kakao;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.BASE_URL;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.CLIENT_ID;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.KAKAO_OAUTH_QUERY_STRING;
import static io.urdego.urdego_user_service.common.properties.OAuthProperty.REDIRECT_URI;


public interface OAuthClient {
	default String getConnectionUrl(){
		return BASE_URL + String.format(KAKAO_OAUTH_QUERY_STRING,CLIENT_ID,REDIRECT_URI);
	}
}
