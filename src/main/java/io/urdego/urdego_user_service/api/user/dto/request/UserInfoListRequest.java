package io.urdego.urdego_user_service.api.user.dto.request;

import io.urdego.urdego_user_service.domain.entity.User;

import java.util.List;

public record UserInfoListRequest(
        List<Long> userIds
) {
}
