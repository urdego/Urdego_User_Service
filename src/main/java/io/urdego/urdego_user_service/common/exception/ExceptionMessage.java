package io.urdego.urdego_user_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    //common
    ReLogin_Fail("재 로그인 실패.",HttpStatus.INTERNAL_SERVER_ERROR, "ReLogin Fail."),
    //user
    NOT_FOUND_USER("유효하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND,"Not found UserId."),
    INVALID_NICKNAME("부적절한 닉네임 입니다.", HttpStatus.BAD_REQUEST, " Nickname contains inappropriate words."),
    DUPLICATED_NICKNAME("이미 사용중인 닉네임 입니다.", HttpStatus.CONFLICT, "This nickname already use."),

    //userCharacter,
    DUPLICATED_CHARACTER("이미 보유중인 케릭터 입니다.", HttpStatus.CONFLICT, "This character already have."),
    NOT_FOUND_CHARACTER("보유하지 않은 캐릭터 입니다.", HttpStatus.BAD_REQUEST, "This character does not yours."),
    INVALID_ACTICVE_CHARACTER("이미 사용 중인 캐릭터 입니다.", HttpStatus.BAD_REQUEST, "This character already use."),

    //character
    INVALID_CHARACTER("찾을 수 없는 캐릭터 입니다.", HttpStatus.BAD_REQUEST, "This character does not exist.");

    private final String text;
    private final HttpStatus status;
    private final String title;
}
