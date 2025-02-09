package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class NotFoundUserNicknameException extends BaseException {
    public static final BaseException EXCEPTION = new NotFoundUserNicknameException();

    private NotFoundUserNicknameException() {
        super(ExceptionMessage.NOT_FOUND_USER_NICKNAME);
    }
}
