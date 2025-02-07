package io.urdego.urdego_user_service.common.exception.character;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class InvalidCharacterException extends BaseException {
    public static final BaseException EXCEPTION = new InvalidCharacterException();

    private InvalidCharacterException() {
        super(ExceptionMessage.INVALID_CHARACTER);
    }
}
