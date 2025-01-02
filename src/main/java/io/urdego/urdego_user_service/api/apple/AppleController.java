package io.urdego.urdego_user_service.api.apple;

import io.urdego.urdego_user_service.api.apple.dto.AppleAuthResponse;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.infra.apple.AppleTokenService;
import io.urdego.urdego_user_service.infra.apple.AppleUserService;
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
    private final AppleUserService appleUserService;
    // JWT 발급 관련

    @PostMapping("/callback")
    public ResponseEntity<AppleAuthResponse> handleAppleCallback(@RequestParam String code) throws Exception {
        AppleTokenReponse appleTokenReponse = appleTokenService.requestToken(code);
        User user = appleUserService.processIdToken(appleTokenReponse.id_token());
        String jwt = null;
        return ResponseEntity.ok(AppleAuthResponse.of(jwt, user));
    }
}
