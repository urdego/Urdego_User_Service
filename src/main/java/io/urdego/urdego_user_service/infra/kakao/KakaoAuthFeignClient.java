package io.urdego.urdego_user_service.infra.kakao;

import io.urdego.urdego_user_service.infra.kakao.dto.KakaoTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "kakao-auth-client",
		url = "https://kauth.kakao.com"
)
public interface KakaoAuthFeignClient {

	@PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
	KakaoTokenDto getAccessToken(
			@RequestParam("grant_type") String grantType,
			@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("code") String code
			//@RequestParam("client_secret") String clientSecret,
	);
}
