package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;

public interface UserService {
	//save
	Long signUp(UserSignUpRequest userSignUpRequest);

	//delete
	void deleteUser(Long id, String drawalRequest);

	//read
	UserResponse findByUserId(Long userId);
	UserResponse findByNickname(String nickname);

}
