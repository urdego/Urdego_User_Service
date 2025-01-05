package io.urdego.urdego_user_service.api.kakao;

import io.urdego.urdego_user_service.api.kakao.dto.KakaoConnectionResponse;
import io.urdego.urdego_user_service.auth.jwt.Tokens;
import io.urdego.urdego_user_service.common.TokenUtils;
import io.urdego.urdego_user_service.domain.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
	private final OAuthService oAuthService;

	@GetMapping("/connection")
	public KakaoConnectionResponse getKakaoConnection(){
		KakaoConnectionResponse kakaoConnectionResponse =
				KakaoConnectionResponse.from(oAuthService.getConnectionUrl());
		System.out.println(oAuthService.getConnectionUrl());
		return kakaoConnectionResponse;
	}

	@GetMapping("/callback")
	public ResponseEntity<String> login(@RequestParam("code") String code){
		String accessToken = oAuthService.login(code);
		return ResponseEntity.ok("AccessToken : " + accessToken);
	}
}

