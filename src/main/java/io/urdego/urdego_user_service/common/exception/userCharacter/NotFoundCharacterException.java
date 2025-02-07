package io.urdego.urdego_user_service.common.exception.userCharacter;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class NotFoundCharacterException extends BaseException {
    public static final BaseException EXCEPTION = new NotFoundCharacterException();

    private NotFoundCharacterException() {
        super(ExceptionMessage.NOT_FOUND_CHARACTER);
    }
}
