/*
package io.urdego.urdego_user_service.auth.jwt;

import io.urdego.urdego_user_service.auth.redis.RedisService;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;
import io.urdego.urdego_user_service.common.exception.jwt.TokenRefreshException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public TokenRes createJwt(String platformId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(platformId, null, null);
        TokenRes tokenRes = jwtUtil.generateToken(platformId);
        String refreshToken = tokenRes.refreshToken();

        redisService.setValues(refreshToken, platformId, Duration.ofDays(14));

        return tokenRes;
    }

    public TokenRes reissueToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new TokenRefreshException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }

        String platformId = redisService.getValues(refreshToken);
        if (platformId == null) {
            throw new TokenRefreshException(ExceptionMessage.REFRESH_TOKEN_NOT_FOUND);
        }

        redisService.deleteValues(refreshToken);

        TokenRes newTokenRes = jwtUtil.generateToken(platformId);
        String newRefreshToken = newTokenRes.refreshToken();

        redisService.setValues(newRefreshToken, platformId, Duration.ofDays(14));

        return newTokenRes;
    }

    public void logout(String refreshToken) {
        redisService.deleteValues(refreshToken);
    }

}
*/
