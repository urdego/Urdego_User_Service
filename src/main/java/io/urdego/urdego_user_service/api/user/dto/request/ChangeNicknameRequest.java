package io.urdego.urdego_user_service.api.user.dto.request;

import io.urdego.urdego_user_service.common.enums.NicknameVerificationResult;

public record ChangeNicknameRequest(
        String newNickname,
        NicknameVerificationResult verificationResult
) {
}
