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

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType());
		List<User> userList = userRepository.findByName(userSignUpRequest.nickname());
		int nicknameNumber = userList.size() + 1;
		if(checkLoginUser(userSignUpRequest.email(), userSignUpRequest.platformId())){
			User existingUser = userRepository.findByEmailAndPlatformType(userSignUpRequest.email(),platformType).orElseThrow(()-> NotFoundUserException.EXCEPTION);

			//삭제된 회원일 경우
			// TODO Query DSL?
			if(existingUser.getIsDeleted().equals(Boolean.TRUE)){
				existingUser.rejoin(userSignUpRequest.platformId());
				existingUser.initExp();
				userRepository.save(existingUser);
				return UserResponse.from(existingUser);
			}
			// 회원가입 하지 않고 로그인일 경우
			if(existingUser.getPlatformId().equals(userSignUpRequest.platformId()) && existingUser.getPlatformType().equals(platformType)){
				return UserResponse.from(existingUser);
			}

		}
		// 회원가입
		User newUser = userRepository.save(User.create(userSignUpRequest, nicknameNumber));
		return UserResponse.from(newUser);
	}

	@Override
	public UserResponse findByUserId(Long userId) {
		User user = readByUserId(userId);
		return UserResponse.from(user);
	}

	@Override
	public void deleteUser(Long userId, String drawalRequest) {
		User user = readByUserId(userId);
		user.setRoleAndDrawalReason(drawalRequest);
		userRepository.save(user);
	}

	@Override
	public UserResponse updateNickname(Long userId, String newNickname) {
		User user = readByUserId(userId);
		if(userRepository.existsByNicknameAndIsDeletedFalse(newNickname)){
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
		User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		return user;
	}

	private boolean checkLoginUser(String email, String platformId) {
		if(userRepository.existsByEmailAndPlatformId(email, platformId)){
			return true;
		}
		return false;
	}


}
