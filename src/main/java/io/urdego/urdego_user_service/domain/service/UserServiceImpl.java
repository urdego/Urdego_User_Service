package io.urdego.urdego_user_service.domain.service;

import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.constant.UserResponse;
import io.urdego.urdego_user_service.domain.entity.constant.UserSignUpRequest;
import io.urdego.urdego_user_service.domain.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public Long signUp(UserSignUpRequest userSignUpRequest) {
		User user = userRepository.save(
				User.builder()
						.nickname(userSignUpRequest.nickname())
						.build()
		);
		return user.getId();
	}

	@Override
	public UserResponse findByUserId(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return UserResponse.builder()
				.userId(user.getId())
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
}
