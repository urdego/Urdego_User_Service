package io.urdego.urdego_user_service.api;

import io.urdego.urdego_user_service.api.dto.DrawalRequest;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.dto.UserResponse;
import io.urdego.urdego_user_service.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-service")
public class UserController {
	private final UserService userService;

	//회원 정보 조회 (단일)
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
		UserResponse response = userService.findByUserId(userId);
		return ResponseEntity.ok(response);
	}

	//회원 탈퇴
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
			@RequestBody DrawalRequest drawalRequest) {

		return ResponseEntity.ok().build();
	}
}
