package io.urdego.urdego_user_service.api.user.dto.request;

public record ChangeNicknameRequest(
        String newNickname,
        String verificationResult
) {
}
