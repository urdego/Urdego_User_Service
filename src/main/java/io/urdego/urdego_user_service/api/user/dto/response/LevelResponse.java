package io.urdego.urdego_user_service.api.user.dto.response;

import io.urdego.urdego_user_service.domain.entity.User;

import java.util.logging.Level;

public record LevelResponse(
        Long userId,
        int level,
        Long totalExp,
        boolean isLevelUp
) {
    public static LevelResponse from(User user, boolean isLevelUp) {
        return new LevelResponse(
                user.getId(),
                user.getLevel(),
                user.getExp(),
                isLevelUp
        );
    }
}
