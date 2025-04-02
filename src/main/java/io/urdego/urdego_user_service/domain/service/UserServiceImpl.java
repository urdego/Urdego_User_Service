package io.urdego.urdego_user_service.domain.service;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserSimpleResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import io.urdego.urdego_user_service.domain.service.components.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	//학습 임계값
	private static final float THRESHOLD = 0.5f;

	//Repository
	private final UserRepository userRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final GameCharacterRepository gameCharacterRepository;

	//Components
	private final LevelCalculator levelCalculator;
	private final UserReader userReader;
	private final UserCommander userCommander;
	private final UserDeleter userDeleter;
	private final UserValidator userValidator;

	private final UserCharacterReader userCharacterReader;
	private final UserCharacterCommander userCharacterCommander;

	@Override
	public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType());

		if(userValidator.checkSignUpUser(userSignUpRequest.email(), platformType)){
			User existingUser = userReader.findByEmailAndPlatformType(userSignUpRequest.email(), platformType);

			//삭제된 회원일 경우
			if(userValidator.checkDeletedUser(existingUser)){
				return UserResponse.from(userCommander.reSignUp(existingUser,userSignUpRequest));
			}
			// 회원가입 하지 않고 로그인일 경우
			if(existingUser.getPlatformId().equals(userSignUpRequest.platformId())
					&& existingUser.getPlatformType().equals(platformType)){
				return UserResponse.from(existingUser);
			}
		}
		// 신규 회원가입
		User newUser = userCommander.signUp(userSignUpRequest);
		return UserResponse.from(newUser);
	}

	@Override
	public UserResponse findByUserId(Long userId) {
		User user = userReader.readByUserId(userId);
		return UserResponse.from(user);
	}

	@Override
	public UserSimpleResponse readUserInfo(Long userId) {
		User user = userReader.readByUserId(userId);
		return UserSimpleResponse.from(user);
	}

	@Override
	public List<UserSimpleResponse> readUserInfoList(List<Long> userIds) {
		return userReader.readAlltoList(userIds);
	}

	@Override
	public void deleteUser(Long userId, String drawalRequest) {
		userDeleter.delete(userId, drawalRequest);
	}

	@Override
	public UserResponse updateNickname(Long userId, String newNickname)throws OrtException{
		return UserResponse.from(userCommander.updateNickname(userId, newNickname));
	}

	@Override
	public UserCharacterResponse updateActiveCharacter(Long userId, ChangeCharacterRequest request) {
		User user = userReader.readByUserId(userId);
		return userCharacterCommander.updateActiveCharacter(user, request);
	}

	@Override
	public UserCharacterResponse addCharacter(Long userId, ChangeCharacterRequest request) {
		User user = userReader.readByUserId(userId);
		return userCharacterCommander.addCharacter(user, request);
	}

	@Override
	public UserResponse searchByNickname(String nickname) {
		return UserResponse.from(userReader.findByNicknameAndIsDeletedFalse(nickname));
	}

	@Override
	public List<UserResponse> searchByWord(String word) {
		return userReader.findByWord(word).stream().map(UserResponse::from).toList();
	}

	@Override
	@Transactional
	public List<LevelResponse> addExp(List<ExpRequest> requests) {
		List<LevelResponse> responses = userCommander.saveExp(requests);
		return responses;
	}
}
