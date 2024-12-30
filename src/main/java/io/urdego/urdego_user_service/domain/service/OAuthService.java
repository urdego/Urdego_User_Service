package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.auth.Tokens;

public interface OAuthService {
	// 로그인 URL 생성
	String getConnectionUrl();

	//Tokens login(String code);
	// access Token 받아오기

	// refresh Token 갱신

	// access Token 무효화 (회원 탈퇴 시)
}
