package io.urdego.urdego_user_service.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.DrawalRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.ChangeCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
//import io.urdego.urdego_user_service.auth.jwt.JwtService;
import io.urdego.urdego_user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service")
public class UserController {
	private final UserService userService;
	//private final JwtService jwtService;

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
			@ApiResponse(responseCode = "200", description = "user 조회 성공", content = @Content(schema = @Schema(implementation = UserResponse.class)))
	)
	@Operation(summary = "회원 정보 조회(단일)", description = "userId로 사용자 정보를 조회")
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
		UserResponse response = userService.findByUserId(userId);
		return ResponseEntity.ok(response);
	}

	//회원 탈퇴
	@DeleteMapping("/users/{userId}")
	@Operation(summary = "회원 탈퇴 DB삭제", description = "userId로 회원정보 softDelete")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
										   @RequestBody DrawalRequest drawalRequest) {
		userService.deleteUser(userId, drawalRequest.withDrwalReason());
		return ResponseEntity.ok().build();
	}

	//회원가입 시 DB 저장
	@Operation(summary = "회원정보 저장", description = "회원정보 저장")
	@PostMapping("/users/save")
	@ApiResponse(responseCode = "200", description = "user 가입성공", content = @Content(schema = @Schema(implementation = UserResponse.class)))
	public ResponseEntity<UserResponse> saveUser(@RequestBody UserSignUpRequest userSignUpRequest) {
		return ResponseEntity.ok(userService.saveUser(userSignUpRequest));
	}


	//닉네임 변경
	@PostMapping("users/nickname/{userId}")
	@ApiResponse(responseCode = "200", description = "응답 예시 : changedNickname111")
	@Operation(summary = "닉네임 변경", description = "중복확인이 된 닉네임으로 변경")
	public ResponseEntity<String> changeNickname(@PathVariable("userId") Long userId,
												 @RequestParam String newNickname) {
		UserResponse response = userService.updateNickname(userId,newNickname);
		return ResponseEntity.ok(response.nickname());
	}

	//캐릭터 변경
	@PostMapping("users/character/{userId}")
	@ApiResponse(responseCode = "캐릭터 변경", description = "응답 예시 : userId, FIRST")
	@Operation(summary = "캐릭터 변경", description = "캐릭터 변경 사항 저장")
	public ResponseEntity<ChangeCharacterResponse> changeCharacter(@PathVariable("userId") Long userId,
																   @RequestBody ChangeCharacterRequest changeCharacterRequest) {
		ChangeCharacterResponse response = userService.updateCharacter(userId, changeCharacterRequest);
		return ResponseEntity.ok(response);
	}
}
