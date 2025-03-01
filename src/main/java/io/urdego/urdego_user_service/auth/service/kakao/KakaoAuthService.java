package io.urdego.urdego_user_service.auth.service.kakao;

import io.urdego.urdego_user_service.auth.jwt.TokenRes;

public interface KakaoAuthService {
	// 로그인 URL 생성
	String getConnectionUrl();

	TokenRes kakaoLogin(String code);
	// access Token 받아오기

	// refresh Token 갱신

	// access Token 무효화 (회원 탈퇴 시)
}
