package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.domain.entity.dto.UserResponse;
import io.urdego.urdego_user_service.domain.entity.dto.UserSignUpRequest;

public interface UserService {
	//save
	Long signUp(UserSignUpRequest userSignUpRequest);

	//delete
	UserResponse deleteUser(Long id);

	//read
	UserResponse findByUserId(Long userId);
	UserResponse findByNickname(String nickname);


}
