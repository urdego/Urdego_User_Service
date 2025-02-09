package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class InvalidActiveCharacterException extends BaseException {
    public static final BaseException EXCEPTION = new InvalidActiveCharacterException();

    private InvalidActiveCharacterException() {
        super(ExceptionMessage.INVALID_ACTICVE_CHARACTER);
    }
}
