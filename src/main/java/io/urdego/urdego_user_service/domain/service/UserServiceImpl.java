package io.urdego.urdego_user_service.domain.service;

import feign.FeignException;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.ChangeCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.exception.character.InvalidCharacterException;
import io.urdego.urdego_user_service.common.exception.user.DuplicatedNicknameUserException;
import io.urdego.urdego_user_service.common.exception.user.InvalidActiveCharacterException;
import io.urdego.urdego_user_service.common.exception.user.NotFoundUserException;
import io.urdego.urdego_user_service.common.exception.user.ReLoginFailException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.NotFoundCharacterException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final GameCharacterRepository gameCharacterRepository;

	@Override
	public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType());

		if(checkLoginUser(userSignUpRequest.email(), platformType)){
			User existingUser = userRepository.findByEmailAndPlatformType(userSignUpRequest.email(), platformType).orElseThrow(()-> NotFoundUserException.EXCEPTION);

			//삭제된 회원일 경우
			// TODO Query DSL?
			if(existingUser.getIsDeleted().equals(Boolean.TRUE)){
				existingUser.initUserInfo(userSignUpRequest.platformId());
				initActiveCharacter(existingUser);
				userRepository.save(existingUser);
				return UserResponse.from(existingUser);
			}
			// 회원가입 하지 않고 로그인일 경우
			if(existingUser.getPlatformId().equals(userSignUpRequest.platformId()) && existingUser.getPlatformType().equals(platformType)){
				return UserResponse.from(existingUser);
			}
		}
		// 신규 회원가입
		User newUser = craeteNewUser(userSignUpRequest);
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
		UserCharacter userCharacter = userCharacterRepository.findByUser(user)
				.orElseThrow(()-> NotFoundUserException.EXCEPTION);
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
	public UserCharacterResponse updateActiveCharacter(Long userId, ChangeCharacterRequest request) {
		User user = readByUserId(userId);
		GameCharacter changeCharacter = gameCharacterRepository.findByName(request.characterName())
				.orElseThrow(()-> InvalidCharacterException.EXCEPTION);
		// 바꾸고자 하는 캐릭터가 보유한 캐릭터에 있는지? 없으면 에러!!
		for(int i = 0; i < user.getOwnedCharacters().size(); i++){
			if(!user.getOwnedCharacters().get(1).getCharacter().getName().equals(request.characterName())){
				throw NotFoundCharacterException.EXCEPTION;
			}
		}

		// 현재 사용 중인 캐릭터와 동일하지 않은지?
		if(user.getActiveCharacter().equals(changeCharacter)){
			throw InvalidActiveCharacterException.EXCEPTION;
		}

		//저장
		user.changeActiveCharacter(changeCharacter);
		userRepository.save(user);
		return UserCharacterResponse.from(user);
	}

	@Override
	public UserCharacterResponse addCharacter(Long userId, ChangeCharacterRequest request) {
		User user = readByUserId(userId);
		GameCharacter addGameCharacter = gameCharacterRepository.findByName(request.characterName())
				.orElseThrow(()-> InvalidCharacterException.EXCEPTION);

		log.info("characterId : {}",addGameCharacter.getId());

		if(userCharacterRepository.existsByUserAndCharacter(user, addGameCharacter)){
			throw DuplicatedCharacterUserException.EXCEPTION;
		}

		UserCharacter userCharacter = new UserCharacter(user, addGameCharacter);
		user.addCharacter(userCharacter);
		userRepository.save(user);

		return UserCharacterResponse.from(user);
	}

	// 공통 component
	// userId 검증
	private User readByUserId(Long userId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		return user;
	}

	private boolean checkLoginUser(String email, PlatformType platformType) {
		if(userRepository.existsByEmailAndPlatformType(email, platformType)){
			return true;
		}
		return false;
	}

	// 신규 회원가입
	private User craeteNewUser(UserSignUpRequest userSignUpRequest) {
		List<User> userList = userRepository.findByName(userSignUpRequest.nickname());
		int nicknameNumber = userList.size() + 1;
		User newUser = User.create(userSignUpRequest,nicknameNumber);

		UserCharacter userCharacter = initActiveCharacter(newUser);

		userRepository.save(newUser);
		userCharacterRepository.save(userCharacter);

		return newUser;
	}

	//기본 캐릭터로 초기화
	private UserCharacter initActiveCharacter(User user){
		GameCharacter basicCharacter = gameCharacterRepository.findById(1L).orElse(null);
		//탈퇴 후 재 로그인 시
		//TODO 탈퇴 후 재로그인 시 에러 발생 !!
		//왜냐면 재 로그인 시 이미 UserCharacters 테이블에 userId가 있음
		if(!user.getOwnedCharacters().isEmpty()){
			UserCharacter existUser = userCharacterRepository.findByUser(user)
					.orElseThrow(()-> ReLoginFailException.EXCEPTION);
			return existUser;
		}
		UserCharacter userCharacter = new UserCharacter(user, basicCharacter);
		user.changeActiveCharacter(basicCharacter);
		user.getOwnedCharacters().add(userCharacter);
		return userCharacter;
	}


}
