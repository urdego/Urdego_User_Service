package io.urdego.urdego_user_service.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.urdego.urdego_user_service.api.user.dto.request.*;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserSimpleResponse;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import io.urdego.urdego_user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service")
public class UserController {
	private final UserService userService;
	private final UserRepository userRepository;
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

	//회원 정보 조회 (리스트)
	@GetMapping("/users")
	public ResponseEntity<List<UserSimpleResponse>> getUsers(@RequestBody UserInfoListRequest request) {
		return ResponseEntity.ok().body(userService.readUserInfoList(request.userIds()));
	}


	//회원 탈퇴
	@DeleteMapping("/users/{userId}")
	@Operation(summary = "회원 탈퇴 DB삭제", description = "userId로 회원정보 softDelete")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
										   @RequestBody DrawalRequest drawalRequest) {
		userService.deleteUser(userId, drawalRequest.withDrawalReason());
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
	@ApiResponse(responseCode = "200", description = "응답 예시 : changedNickname111",content = @Content(schema = @Schema(implementation = UserResponse.class)))
	@Operation(summary = "닉네임 변경", description = "중복확인이 된 닉네임으로 변경")
	public ResponseEntity<String> changeNickname(@PathVariable("userId") Long userId,
												 @RequestBody ChangeNicknameRequest request) {
		UserResponse response = userService.updateNickname(userId, request.newNickname());
		return ResponseEntity.ok(response.nickname());
	}

	//캐릭터 변경
	@PostMapping("users/character/change/{userId}")
	@ApiResponse(responseCode = "200", description = "응답 예시 : activeCharacter : BASIC" , content = @Content(schema = @Schema(implementation = UserCharacterResponse.class)))
	@Operation(summary = "캐릭터 변경", description = "캐릭터 변경 사항 저장")
	public ResponseEntity<UserCharacterResponse> changeCharacter(@PathVariable("userId") Long userId,
																   @RequestBody ChangeCharacterRequest changeCharacterRequest) {
		UserCharacterResponse response = userService.updateActiveCharacter(userId, changeCharacterRequest);
		return ResponseEntity.ok(response);
	}

	//캐릭터 획득 (유저에게)
	@PostMapping("users/character/add/{userId}")
	@ApiResponse(responseCode = "200", description = "응답 예시 : ownedCharacter [...]" , content = @Content(schema = @Schema(implementation = UserCharacterResponse.class)))
	@Operation(summary = "캐릭터 획득(유저 별)", description = "유저가 소유한 캐릭터 추가")
	public ResponseEntity<UserCharacterResponse> addCharacter(@PathVariable("userId") Long userId,
															  @RequestBody ChangeCharacterRequest reqeust){
		UserCharacterResponse response = userService.addCharacter(userId, reqeust);
		return ResponseEntity.ok(response);
	}

	@GetMapping("users/search")
	@ApiResponse(responseCode = "200", description = "응답 예시 : user{...}" , content = @Content(schema = @Schema(implementation = UserResponse.class)))
	@Operation(summary = "닉네임으로 유저 찾기", description = "닉네임으로 유저를 찾는다.")
	public ResponseEntity<UserResponse> searchUser(@RequestParam("nickname") String nickname) {
		UserResponse response = userService.searchByNickname(nickname);
		return ResponseEntity.ok(response);
	}

	@GetMapping("users/search/word")
	@ApiResponse(responseCode = "200", description = "응답 예시 : user{...}" , content = @Content(schema = @Schema(implementation = UserResponse.class)))
	@Operation(summary = "단어가 포함된 닉네임의 유저 찾기", description = "단어를 포함하는 닉네임의 유저를 찾는다.")
	public ResponseEntity<List<UserResponse>> searchUserByWord(@RequestParam String word) {
		List<UserResponse> responses = userService.searchByWord(word);
		return ResponseEntity.ok(responses);
	}
}
