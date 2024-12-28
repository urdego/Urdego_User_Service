package io.urdego.urdego_user_service.infra.kakao;

import io.urdego.urdego_user_service.infra.feign.config.FeignConfig;
import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
		name = "kakao-auth-client",
		url = "https://kauth.kakao.com",
		configuration = FeignConfig.class
)
public interface KakaoAuthClient {
	@PostMapping(
			"/oauth/token?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&code={CODE}&grant_type=authorization_code")
	KakaoTokenResponse auth(
			@PathVariable("CLIENT_ID") String clientId,
			@PathVariable("REDIRECT_URI") String redirectUri,
			@PathVariable("CODE") String code);
}
