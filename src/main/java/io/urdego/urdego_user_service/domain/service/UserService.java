package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserSimpleResponse;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;

import java.util.List;

public interface UserService {
	//create
	UserResponse saveUser(UserSignUpRequest userSignUpRequest);

	//read
	UserResponse findByUserId(Long userId);

	UserSimpleResponse readUserInfo(Long userId);

	List<UserSimpleResponse> readUserInfoList(List<Long> userIds);

	//update Nickname
	UserResponse updateNickname(Long userId, String newNickname);

	//delete
	void deleteUser(Long id, String drawalRequest);

	//update Character
	UserCharacterResponse updateActiveCharacter(Long userId, ChangeCharacterRequest changeCharacterRequest);

	//add Character
	UserCharacterResponse addCharacter(Long userId,ChangeCharacterRequest changeCharacterRequest);

	//findByNickname
	UserResponse searchByNickname(String nickname);

	List<UserResponse> searchByWord(String word);

	List<LevelResponse> addExp(List<ExpRequest> requests);

	UserCharacter levelReword(User user, int characterIndex);
}
