package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeNicknameRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.enums.NicknameVerificationResult;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.common.exception.user.InappropriateNicknameUserException;
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
	public NicknameVerificationResult verifyNickname(String nickname) {
		if(userRepository.existsByNickname(nickname)) {
			return NicknameVerificationResult.DUPLICATE;
		}
		return NicknameVerificationResult.PERMIT;
	}

	@Override
	public void deleteUser(Long userId, String drawalRequest) {
		User user = readByUserId(userId);
		user.setRoleAndDrwalReason(drawalRequest);
		userRepository.save(user);
	}

	@Override
	public UserResponse updateNickname(Long userId, ChangeNicknameRequest changeNicknameRequest) {
		User user = readByUserId(userId);
		if(!changeNicknameRequest.verificationResult().equals("PERMIT")) {
			throw InappropriateNicknameUserException.EXCEPTION;
		}
		user.updateNickname(changeNicknameRequest.newNickname());
		UserResponse response = UserResponse.from(userRepository.save(user));
		return response;
	}

	// 공통
	private User readByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		return user;
	}
}
