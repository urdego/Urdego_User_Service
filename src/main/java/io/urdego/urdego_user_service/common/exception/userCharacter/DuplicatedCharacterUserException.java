package io.urdego.urdego_user_service.common.exception.userCharacter;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class DuplicatedCharacterUserException extends BaseException {
    public static final BaseException EXCEPTION = new DuplicatedCharacterUserException();

    private DuplicatedCharacterUserException() {
        super(ExceptionMessage.DUPLICATED_CHARACTER);
    }
}
