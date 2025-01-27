package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.ChangeCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;

public interface UserService {
	//create
	UserResponse saveUser(UserSignUpRequest userSignUpRequest);

	//read
	UserResponse findByUserId(Long userId);

	//update Nickname
	UserResponse updateNickname(Long userId, String newNickname);

	//delete
	void deleteUser(Long id, String drawalRequest);

	//update Character
	ChangeCharacterResponse updateCharacter(Long userId, ChangeCharacterRequest changeCharacterRequest);

}
