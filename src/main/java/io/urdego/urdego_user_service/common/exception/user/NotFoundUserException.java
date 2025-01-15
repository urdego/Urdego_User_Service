package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class NotFoundUserException extends BaseException {
    public static final BaseException EXCEPTION = new NotFoundUserException();

    private NotFoundUserException() {
        super(ExceptionMessage.NOT_FOUND_USER);
    }
}
