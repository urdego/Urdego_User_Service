package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.ChangeCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.enums.CharacterType;
import io.urdego.urdego_user_service.common.enums.NicknameVerificationResult;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.common.exception.user.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.common.exception.user.DuplicatedNicknameUserException;
import io.urdego.urdego_user_service.common.exception.user.NotFoundUserException;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
		User user = userRepository.save(User.create(userSignUpRequest));
		return UserResponse.from(user);
	}

	@Override
	public UserResponse findByUserId(Long userId) {
		User user = readByUserId(userId);
		return UserResponse.from(user);
	}

	@Override
	public void deleteUser(Long userId, String drawalRequest) {
		User user = readByUserId(userId);
		user.setRoleAndDrwalReason(drawalRequest);
		userRepository.save(user);
	}

	@Override
	public UserResponse updateNickname(Long userId, String newNickname) {
		User user = readByUserId(userId);
		if(userRepository.existsByNickname(newNickname)){
			throw DuplicatedNicknameUserException.EXCEPTION;
		}
		user.updateNickname(newNickname);
		UserResponse response = UserResponse.from(userRepository.save(user));
		return response;
	}

	@Override
	public ChangeCharacterResponse updateCharacter(Long userId, ChangeCharacterRequest changeCharacterRequest) {
		CharacterType newCharacterType = CharacterType.valueOf(changeCharacterRequest.characterName());
		User user = readByUserId(userId);
		//TODO 잘못된 값이 들어오면 어쩌지? 그것도 해야되나?

		if(user.getCharacterType().equals(newCharacterType)){
			throw DuplicatedCharacterUserException.EXCEPTION;
		}
		user.ChangeCharacterType(newCharacterType);
		userRepository.save(user);
		ChangeCharacterResponse response = ChangeCharacterResponse.of(user.getId(), user.getCharacterType());
		return response;
	}

	// 공통
	// userId 검증
	private User readByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		return user;
	}


}
