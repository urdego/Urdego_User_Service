package io.urdego.urdego_user_service.domain.entity.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
	private Long id;

	@JsonProperty("connected_at")
	private String connectedAt;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Data
	public static class KakaoAccount {

		@JsonProperty("email")
		private String email;
		//사진
		private Profile profile;

		@Data
		public static class Profile {

			@JsonProperty("nickname")
			private String nickname;

			@JsonProperty("profile_image_url")
			private String profileImage;
		}

	}
}
