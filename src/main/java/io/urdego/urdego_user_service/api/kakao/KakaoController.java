package io.urdego.urdego_user_service.api.kakao;

import io.urdego.urdego_user_service.api.kakao.dto.KakaoConnectionResponse;
import io.urdego.urdego_user_service.auth.jwt.TokenRes;
import io.urdego.urdego_user_service.auth.service.kakao.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service/kakao")
@Slf4j
public class KakaoController {
	private final KakaoAuthService kakaoAuthService;

	@GetMapping("/connection")
	public KakaoConnectionResponse getKakaoConnection(){
		KakaoConnectionResponse kakaoConnectionResponse =
				KakaoConnectionResponse.from(kakaoAuthService.getConnectionUrl());
		System.out.println(kakaoAuthService.getConnectionUrl());
		return kakaoConnectionResponse;
	}

	@GetMapping("/callback")
	public ResponseEntity<TokenRes> login(@RequestParam("code") String code){
		TokenRes jwtToken = kakaoAuthService.kakaoLogin(code);
		return ResponseEntity.ok(jwtToken);
	}
}

