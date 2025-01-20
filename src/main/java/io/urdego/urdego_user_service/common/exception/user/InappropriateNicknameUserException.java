package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class InappropriateNicknameUserException extends BaseException {
    public static final BaseException EXCEPTION = new InappropriateNicknameUserException();

    private InappropriateNicknameUserException() {
        super(ExceptionMessage.INAPPROPRIATE_NICKNAME);
    }
}
