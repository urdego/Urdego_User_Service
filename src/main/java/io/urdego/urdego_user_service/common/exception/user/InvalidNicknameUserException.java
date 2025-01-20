package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class InvalidNicknameUserException extends BaseException {
    public static final BaseException EXCEPTION = new InvalidNicknameUserException();

    private InvalidNicknameUserException() {
        super(ExceptionMessage.INVALID_NICKNAME);
    }
}
