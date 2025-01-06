package io.urdego.urdego_user_service.auth.service;

public interface KakaoAuthService {
	// 로그인 URL 생성
	String getConnectionUrl();

	String login(String code);
	// access Token 받아오기

	// refresh Token 갱신

	// access Token 무효화 (회원 탈퇴 시)
}
