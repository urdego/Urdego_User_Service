package io.urdego.urdego_user_service.api.kakao;

import io.urdego.urdego_user_service.domain.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service/kakao")
public class KakaoController {
	private final OAuthService oAuthService;

	/*@GetMapping("/connection")
	public Response<KakaoConnectionResponse> getKakaoConnection(){
		KakaoConnectionResponse kakaoConnectionResponse =
				KakaoConnectionResponse.from()
	}*/
}

