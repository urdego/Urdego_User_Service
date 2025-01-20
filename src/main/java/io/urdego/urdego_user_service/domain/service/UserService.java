package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeNicknameRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.enums.NicknameVerificationResult;

public interface UserService {
	//create
	UserResponse signUp(UserSignUpRequest userSignUpRequest);

	//read
	UserResponse findByUserId(Long userId);

	//Validate Nickname
	NicknameVerificationResult verifyNickname (String nickname);

	//update Nickname
	UserResponse updateNickname(Long userId, ChangeNicknameRequest changeNicknameRequest);

	//delete
	void deleteUser(Long id, String drawalRequest);

}
