package io.urdego.urdego_user_service.api.user;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeNicknameRequest;
import io.urdego.urdego_user_service.api.user.dto.request.DrawalRequest;
import io.urdego.urdego_user_service.api.user.dto.request.TokenRefreshRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
//import io.urdego.urdego_user_service.auth.jwt.JwtService;
import io.urdego.urdego_user_service.auth.jwt.TokenRes;
import io.urdego.urdego_user_service.common.enums.NicknameVerficationResult;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.service.UserService;
import io.urdego.urdego_user_service.domain.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service")
public class UserController {
	private final UserService userService;
	//private final JwtService jwtService;
	private final UserServiceImpl userServiceImpl;

	/*// 토큰 재발급
	@PostMapping("/auth/refresh-token")
	public ResponseEntity<TokenRes> refreshToken(@RequestBody TokenRefreshRequest request) {
		String refreshToken =request.refreshToken();

		TokenRes tokenRes = jwtService.reissueToken(refreshToken);

		// 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenRes.accessToken());
		headers.set("X-Refresh-Token", tokenRes.refreshToken());

		return ResponseEntity.ok().headers(headers).body(tokenRes);
	}*/

	/*@PostMapping("/users/logout")
	public ResponseEntity<String> logout(@RequestBody TokenRefreshRequest request) {
		String refreshToken = request.refreshToken();

		jwtService.logout(refreshToken);

		return ResponseEntity.ok("로그아웃 성공");
	}*/

	//회원 정보 조회 (단일)
	@ApiResponses(
			@ApiResponse( responseCode = "200", description = "user 조회 성공", content = @Content(schema = @Schema(implementation = UserResponse.class)))
	)
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
		UserResponse response = userService.findByUserId(userId);
		return ResponseEntity.ok(response);
	}

	//회원 탈퇴
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
										   @RequestBody DrawalRequest drawalRequest) {
		userService.deleteUser(userId, drawalRequest.withDrwalReason());
		return ResponseEntity.ok().build();
	}

	//회원가입 시 DB 저장
	@PostMapping("/users/save")
	@ApiResponse(responseCode = "200", description = "user 가입성공", content = @Content(schema = @Schema(implementation = UserResponse.class)))
	public ResponseEntity<UserResponse> saveUser(@RequestBody UserSignUpRequest userSignUpRequest) {
		return ResponseEntity.ok(userServiceImpl.signUp(userSignUpRequest));
	}


	//닉네임 중복 확인
	@PostMapping("/users/nickname")
	@ApiResponse(responseCode = "200", description = " 응답 예시 : PERMIN / DUPlICATED")
	public ResponseEntity<String> verifyNickname(@RequestParam("nickname") String nickname) {
		NicknameVerficationResult result = userService.verifyNickname(nickname);
		return ResponseEntity.ok(result.getStatus());
	}

	//닉네임 변경
	@PostMapping("users/nickname/{userId}")
	@ApiResponse(responseCode = "200", description = "응답 예시 : changedNickname111")
	public ResponseEntity<String> changeNickname(@PathVariable("userId") Long userId,
												 @RequestBody ChangeNicknameRequest changeNicknameRequest){
		userService.updateNickname(userId,changeNicknameRequest);
		return ResponseEntity.ok(changeNicknameRequest.newNickname());
	}
}
