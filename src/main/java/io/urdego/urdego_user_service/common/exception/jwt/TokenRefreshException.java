package io.urdego.urdego_user_service.common.exception.jwt;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class TokenRefreshException extends BaseException {
    public TokenRefreshException(ExceptionMessage message) {
        super(message);
    }
}
