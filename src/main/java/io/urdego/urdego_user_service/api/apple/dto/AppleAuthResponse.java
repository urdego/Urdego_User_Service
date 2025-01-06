package io.urdego.urdego_user_service.api.apple.dto;

import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.common.enums.PlatformType;

public record AppleAuthResponse(
        String token,
        Long id,
        String nickname,
        String platformId,
        PlatformType platformType
) {
    public static AppleAuthResponse of(String token, User user) {
        return new AppleAuthResponse(
                token,
                user.getId(),
                user.getNickname(),
                user.getPlatformId(),
                user.getPlatformType()
        );
    }
}
