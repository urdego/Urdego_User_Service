package io.urdego.urdego_user_service.infra.apple;

import io.urdego.urdego_user_service.common.config.FeignConfig;
import io.urdego.urdego_user_service.infra.apple.dto.AppleTokenReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "apple-auth-client",
        url = "${apple.auth.token-url}",
        configuration = FeignConfig.class
)
public interface AppleAuthFeignClient {
    @PostMapping
    AppleTokenReponse auth(
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String authorizationCode,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret
    );
}
