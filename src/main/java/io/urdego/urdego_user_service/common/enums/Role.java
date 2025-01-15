package io.urdego.urdego_user_service.common.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	UNAUTH("미인증"),

	USER("유저"),

	ADMIN("관리자");
	private final String role;

}
