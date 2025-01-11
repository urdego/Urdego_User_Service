package io.urdego.urdego_user_service.infra.apple.dto;

public record AppleTokenReponse(
        String access_token,
        String id_token,
        String token_type,
        Long expires_in
) {
}
