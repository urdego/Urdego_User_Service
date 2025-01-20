package io.urdego.urdego_user_service.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NicknameVerficationResult {
    PERMIT("PERMIT"),
    DUPLICATE("DUPLICATE"),
    INAPPROPRIATENESS("INAPPROPRIATENESS");

    private final String status;
}
