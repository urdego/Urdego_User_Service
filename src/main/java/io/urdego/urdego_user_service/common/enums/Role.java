package io.urdego.urdego_user_service.common.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	UNAUTH("미인증"),

	USER("유저"),

	ADMIN("관리자"),

	DELETED("삭제된 회원");

	private final String role;

	public static Role findByRole(final String role) {
		return Arrays.stream(Role.values())
				.filter(userRole -> userRole.role.equals(role))
				.findFirst()
				.orElse(null);
	}
}
