package io.urdego.urdego_user_service.auth.service.apple;

public interface AppleAuthService {
    String appleLogin(String idToken) throws Exception;
}
