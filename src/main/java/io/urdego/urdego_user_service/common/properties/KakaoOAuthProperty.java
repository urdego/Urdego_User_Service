package io.urdego.urdego_user_service.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOAuthProperty {
	private String baseUrl;
	private String clientId;
	private String redirectUrl;
	private String clientSecret;

	public static final String KAKAO_OAUTH_QUERY_STRING =
			"/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s";
}
