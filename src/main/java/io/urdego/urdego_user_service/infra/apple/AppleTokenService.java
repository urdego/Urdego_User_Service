package io.urdego.urdego_user_service.infra.apple;

import io.urdego.urdego_user_service.infra.apple.dto.AppleTokenReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleTokenService {

    private final AppleAuthFeignClient appleAuthFeignClient;
    private final AppleClientSecretGenerator clientSecretGenerator;

    public AppleTokenReponse requestToken(String authorizationCode) throws Exception {
        String clientSecret = clientSecretGenerator.generateClientSecret();

        return appleAuthFeignClient.auth(
                "authorization_code",
                authorizationCode,
                "https://urdego.vercel.app/api/auth/callback/apple",
                "com.urdego.service",
                clientSecret
        );

    }
}
