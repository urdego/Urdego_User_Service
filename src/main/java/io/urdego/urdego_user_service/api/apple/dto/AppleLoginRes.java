package io.urdego.urdego_user_service.api.apple.dto;

import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.common.enums.PlatformType;

public record AppleLoginRes(
        String token,
        Long id,
        String nickname,
        String platformId,
        PlatformType platformType
) {
    public static AppleLoginRes of(String token, User user) {
        return new AppleLoginRes(
                token,
                user.getId(),
                user.getNickname(),
                user.getPlatformId(),
                user.getPlatformType()
        );
    }
}
