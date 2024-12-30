package io.urdego.urdego_user_service.api.kakao;

import io.urdego.urdego_user_service.api.kakao.dto.KakaoConnectionResponse;
import io.urdego.urdego_user_service.auth.Tokens;
import io.urdego.urdego_user_service.common.TokenUtils;
import io.urdego.urdego_user_service.domain.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service/kakao")
public class KakaoController {
	private final OAuthService oAuthService;

	@GetMapping("/connection")
	public KakaoConnectionResponse getKakaoConnection(){
		KakaoConnectionResponse kakaoConnectionResponse =
				KakaoConnectionResponse.from(oAuthService.getConnectionUrl());
		return kakaoConnectionResponse;
	}

	@GetMapping("/login")
	public ResponseEntity<Void> login(@RequestParam String code){
		Tokens tokens = oAuthService.login(code);
		HttpHeaders headers = TokenUtils.createTokenHeaders(tokens);
		return ResponseEntity.ok().headers(headers).body(null);
	}
}

