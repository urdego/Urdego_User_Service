package io.urdego.urdego_user_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    REFRESH_TOKEN_NOT_FOUND("Refresh Token이 존재하지 않거나 만료되었습니다.", HttpStatus.NOT_FOUND, "Refresh Token Not Found"),
    INVALID_REFRESH_TOKEN("유효하지 않은 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED, "Invalid Refresh Token"),
    NOT_FOUND_USER("유요하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND,"not found UserId");

    private final String text;
    private final HttpStatus status;
    private final String title;
}
