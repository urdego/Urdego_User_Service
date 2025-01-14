package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public Long signUp(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType().toUpperCase());
		User user = userRepository.save(
				User.builder()
						.nickname(userSignUpRequest.nickname())
						.email(userSignUpRequest.email())
						.platformId(userSignUpRequest.platformId())
						.platformType(platformType)
						.role(Role.USER)
						.profileImageUrl(userSignUpRequest.profileImageUrl())
						.build()
		);
		return user.getId();
	}

	@Override
	public UserResponse findByUserId(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return UserResponse.builder()
				.userId(user.getId())
				.email(user.getEmail())
				.platformId(user.getPlatformId())
				.platfromType(user.getPlatformType())
				.role(user.getRole())
				.profileImageUrl(user.getProfileImageUrl())
				.nickname(user.getNickname())
				.build();
	}

	@Override
	public UserResponse findByNickname(String nickname) {
		User user = userRepository.findByNickname(nickname).orElse(null);
		return UserResponse.builder()
				.userId(user.getId())
				.nickname(user.getNickname())
				.build();
	}

	@Override
	public void deleteUser(Long id, String drawalRequest) {
		User user = userRepository.findById(id).orElse(null);
		user.setRoleAndDrwalReason(drawalRequest);
		userRepository.save(user);
	}
}
