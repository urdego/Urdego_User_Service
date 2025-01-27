package io.urdego.urdego_user_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    REFRESH_TOKEN_NOT_FOUND("Refresh Token이 존재하지 않거나 만료되었습니다.", HttpStatus.NOT_FOUND, "Refresh Token Not Found"),
    INVALID_REFRESH_TOKEN("유효하지 않은 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED, "Invalid Refresh Token"),
    NOT_FOUND_USER("유효하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND,"not found UserId."),
    INVALID_NICKNAME("부적절한 닉네임 입니다.", HttpStatus.BAD_REQUEST, " Nickname contains inappropriate words."),
    DUPLICATED_NICKNAME("이미 사용중인 닉네임 입니다.", HttpStatus.CONFLICT, "This nickname already use."),
    DUPLICATED_CHARACTER_TYPE("이미 사용중인 케릭터 입니다.", HttpStatus.CONFLICT, "This character already use.");


    private final String text;
    private final HttpStatus status;
    private final String title;
}
