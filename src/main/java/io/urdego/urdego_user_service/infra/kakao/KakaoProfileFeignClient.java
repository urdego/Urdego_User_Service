package io.urdego.urdego_user_service.infra.kakao;

import io.urdego.urdego_user_service.domain.entity.constant.KakaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
		name = "kakao-profile-client",
		url = "https://kapi.kakao.com"
)
public interface KakaoProfileFeignClient {
	@GetMapping("/v1/oidc/userinfo")
	KakaoProfileResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
