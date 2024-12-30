package io.urdego.urdego_user_service.domain.service;

import static io.urdego.urdego_user_service.common.properties.OAuthProperty.BASE_URL;

import io.urdego.urdego_user_service.auth.OAuthProfile;
import io.urdego.urdego_user_service.auth.Tokens;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.repository.UserRepository;
import io.urdego.urdego_user_service.infra.kakao.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
	private final OAuthClient oAuthClient;
	private final UserRepository userRepository;

	@Override
	public String getConnectionUrl() {
		return oAuthClient.getConnectionUrl();
	}

/*	@Override
	public Tokens login(String code) {
		//@TODO 컴파일 에러 차후 수정 후 재 push 예정
		OAuthProfile oAuthProfile = oAuthClient.getOAuthProfile(code);

	}*/
}
