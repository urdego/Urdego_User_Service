package io.urdego.urdego_user_service.domain.entity.dto;

import lombok.Builder;

public class UserResponse {
	private Long userId;
	private String nickname;

	@Builder
	public UserResponse(Long userId, String nickname) {
		this.userId = userId;
		this.nickname = nickname;
	}
}
