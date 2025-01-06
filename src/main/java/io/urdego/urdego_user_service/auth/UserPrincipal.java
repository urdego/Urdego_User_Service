package io.urdego.urdego_user_service.auth;

import io.urdego.urdego_user_service.domain.entity.constant.Role;

public record UserPrincipal(long userId, Role role) {

	public static UserPrincipal of(long userId, Role role) {
		return new UserPrincipal(userId, role);
	}
}
