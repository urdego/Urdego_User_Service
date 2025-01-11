package io.urdego.urdego_user_service.auth.service.apple;

import io.urdego.urdego_user_service.auth.jwt.TokenRes;

public interface AppleAuthService {
    TokenRes appleLogin(String idToken) throws Exception;
}
