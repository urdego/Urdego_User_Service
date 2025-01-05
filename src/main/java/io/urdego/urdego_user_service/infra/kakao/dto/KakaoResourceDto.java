package io.urdego.urdego_user_service.infra.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record KakaoResourceDto(
		@JsonProperty("id")
		String id,

		@JsonProperty("email")
		String email,

		@JsonProperty("profile_image_url")
		String picture,

		@JsonProperty("nickname")
		String nickname
) {
	@Builder
	public KakaoResourceDto(String id, String email, String picture, String nickname) {
		this.id = id;
		this.email = email;
		this.picture = picture;
		this.nickname = nickname;
	}
}
