package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class DuplicatedNicknameUserException extends BaseException {
    public static final BaseException EXCEPTION = new DuplicatedNicknameUserException();

    private DuplicatedNicknameUserException() {
        super(ExceptionMessage.DUPLICATED_NICKNAME);
    }
}
