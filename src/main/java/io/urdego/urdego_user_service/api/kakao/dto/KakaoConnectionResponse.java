package io.urdego.urdego_user_service.api.kakao.dto;

public record KakaoConnectionResponse(String url) {
	public static KakaoConnectionResponse from(String url){
		return new KakaoConnectionResponse(url);
	}
}
