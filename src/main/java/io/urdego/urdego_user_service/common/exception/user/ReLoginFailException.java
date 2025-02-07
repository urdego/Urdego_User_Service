package io.urdego.urdego_user_service.common.exception.user;

import io.urdego.urdego_user_service.common.exception.BaseException;
import io.urdego.urdego_user_service.common.exception.ExceptionMessage;

public class ReLoginFailException extends BaseException {
    public static final BaseException EXCEPTION = new ReLoginFailException();

    private ReLoginFailException() {
        super(ExceptionMessage.ReLogin_Fail);
    }
}
