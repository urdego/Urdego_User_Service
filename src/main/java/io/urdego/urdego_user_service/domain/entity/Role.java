package io.urdego.urdego_user_service.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	UNAUTH("미인증"),

	USER("유저"),

	ADMIN("관리자"),

	DELETED("삭제된 회원");

	private final String text;
}
