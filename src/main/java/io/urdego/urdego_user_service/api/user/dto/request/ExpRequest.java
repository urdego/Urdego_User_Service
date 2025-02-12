package io.urdego.urdego_user_service.api.user.dto.request;

import java.util.HashMap;
import java.util.List;

public record ExpRequest(
        Long userId,
        Long exp
) {
}
