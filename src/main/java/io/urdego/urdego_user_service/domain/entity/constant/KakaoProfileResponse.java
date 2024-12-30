package io.urdego.urdego_user_service.domain.entity.constant;

import io.urdego.urdego_user_service.auth.OAuthProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoProfileResponse implements OAuthProfile {
	private String nickname;
	private String email;
}
