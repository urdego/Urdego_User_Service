package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.enums.NicknameVerificationResult;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
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
	public UserResponse signUp(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType().toUpperCase());
		User user = userRepository.save(
				User.builder()
						.nickname(userSignUpRequest.nickname())
						.email(userSignUpRequest.email())
						.platformId(userSignUpRequest.platformId())
						.platformType(platformType)
						.role(Role.USER)
						.build()
		);
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

	// 공통
	// userId 검증
	private User readByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		return user;
	}


}
