package io.urdego.urdego_user_service.api.apple;

import io.urdego.urdego_user_service.api.apple.dto.AppleAuthResponse;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.infra.apple.AppleTokenService;
import io.urdego.urdego_user_service.auth.service.apple.AppleAuthServiceImpl;
import io.urdego.urdego_user_service.infra.apple.dto.AppleTokenReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service/apple")
public class AppleController {

    private final AppleTokenService appleTokenService;
    private final AppleAuthServiceImpl appleAuthServiceImpl;
    // JWT 발급 관련

    @PostMapping("/callback")
    public ResponseEntity<String> handleAppleCallback(@RequestParam String code) throws Exception {
        AppleTokenReponse appleTokenReponse = appleTokenService.requestToken(code);
        String accessToken = appleAuthServiceImpl.appleLogin(appleTokenReponse.id_token());
        return ResponseEntity.ok("AccessToken : " + accessToken);
    }
}
