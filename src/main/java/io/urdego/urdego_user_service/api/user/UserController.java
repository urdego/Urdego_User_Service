package io.urdego.urdego_user_service.api.user;

import io.urdego.urdego_user_service.api.user.dto.request.DrawalRequest;
import io.urdego.urdego_user_service.api.user.dto.request.TokenRefreshRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.auth.jwt.JwtService;
import io.urdego.urdego_user_service.auth.jwt.TokenRes;
import io.urdego.urdego_user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service")
public class UserController {
	private final UserService userService;
	private final JwtService jwtService;

	// 토큰 재발급
	@PostMapping("/auth/refresh-token")
	public ResponseEntity<TokenRes> refreshToken(@RequestBody TokenRefreshRequest request) {
		String refreshToken =request.refreshToken();

		TokenRes tokenRes = jwtService.reissueToken(refreshToken);

		// 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenRes.accessToken());
		headers.set("X-Refresh-Token", tokenRes.refreshToken());

		return ResponseEntity.ok().headers(headers).body(tokenRes);
	}

	//회원 정보 조회 (단일)
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
		UserResponse response = userService.findByUserId(userId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/users/logout")
	public ResponseEntity<String> logout(@RequestBody TokenRefreshRequest request) {
		String refreshToken = request.refreshToken();

		jwtService.logout(refreshToken);

		return ResponseEntity.ok("로그아웃 성공");
	}

	//회원 탈퇴
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
										   @RequestBody DrawalRequest drawalRequest) {
		userService.deleteUser(userId, drawalRequest.withDrwalReason());
		return ResponseEntity.ok().build();
	}
}
