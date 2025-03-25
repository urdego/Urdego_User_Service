package io.urdego.urdego_user_service.domain.service;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.BadWordResponse;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.api.user.dto.response.UserSimpleResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.exception.character.InvalidCharacterException;
import io.urdego.urdego_user_service.common.exception.user.*;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.NotFoundCharacterException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import io.urdego.urdego_user_service.infra.model.OnnxInference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	//학습 임계값
	private static final float THRESHOLD = 0.5f;

	private final UserRepository userRepository;
	private final UserCharacterRepository userCharacterRepository;
	private final GameCharacterRepository gameCharacterRepository;
	private final OnnxInference onnxInference;
	private final Tokenizer tokenizer;

	@Override
	public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
		PlatformType platformType = PlatformType.valueOf(userSignUpRequest.platformType());

		if(checkLoginUser(userSignUpRequest.email(), platformType)){
			User existingUser = userRepository.findByEmailAndPlatformType(userSignUpRequest.email(), platformType).orElseThrow(()-> NotFoundUserException.EXCEPTION);

			//삭제된 회원일 경우
			if(existingUser.getIsDeleted().equals(Boolean.TRUE)){
				List<User> userList = userRepository.findByName(userSignUpRequest.nickname());
				int nicknameNumber = userList.size() + 1;
				existingUser.initUserInfo(userSignUpRequest.platformId());
				existingUser.updateNickname(userSignUpRequest.nickname() +"#"+nicknameNumber);
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
	public UserSimpleResponse readUserInfo(Long userId) {
		User user = readByUserId(userId);
		return UserSimpleResponse.from(user);
	}

	@Override
	public List<UserSimpleResponse> readUserInfoList(List<Long> userIds) {
		List<User> users = userRepository.findAllById(userIds);
		List<UserSimpleResponse> responses = new ArrayList<>();
		for(User user : users) {
			UserSimpleResponse response = UserSimpleResponse.from(user);
			responses.add(response);
		}
		return responses;
	}

	@Override
	@Transactional
	public void deleteUser(Long userId, String drawalRequest) {
		User user = readByUserId(userId);
		user.setRoleAndDrawalReason(drawalRequest);
		userCharacterRepository.deleteByUser(user);
		userRepository.save(user);
	}

	@Override
	public UserResponse updateNickname(Long userId, String newNickname)throws OrtException{
		User user = readByUserId(userId);
		if(userRepository.existsByNicknameAndIsDeletedFalse(newNickname)){
			throw DuplicatedNicknameUserException.EXCEPTION;
		}
	    /*if(isProfane(newNickname)){
			throw InvalidNicknameUserException.EXCEPTION;
		}*/
		user.updateNickname(newNickname);
		userRepository.save(user);
		log.info("new nickname : {}", newNickname);
		log.info("real nickname : {}", user.getNickname());
		UserResponse response = UserResponse.from(user);
		return response;
	}

	@Override
	public UserCharacterResponse updateActiveCharacter(Long userId, ChangeCharacterRequest request) {
		User user = readByUserId(userId);
		GameCharacter changeCharacter = gameCharacterRepository.findByName(request.characterName())
				.orElseThrow(()-> InvalidCharacterException.EXCEPTION);
		// 바꾸고자 하는 캐릭터가 보유한 캐릭터에 있는지? 없으면 에러!!
		for(int i = 0; i < user.getOwnedCharacters().size(); i++){

			log.info("characterName : {}", request.characterName());
			log.info("ownedCharacter : {}", user.getOwnedCharacters().get(i).getCharacter().getName());

			if(user.getOwnedCharacters().get(i).getCharacter().getName().equals(request.characterName())){
				// 현재 사용 중인 캐릭터와 동일하지 않은지?
				if(user.getActiveCharacter().equals(changeCharacter)){
					throw InvalidActiveCharacterException.EXCEPTION;
				}

				//저장
				user.changeActiveCharacter(changeCharacter);
				userRepository.save(user);
				return UserCharacterResponse.from(user);
			}
		}
		throw NotFoundCharacterException.EXCEPTION;
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

	@Override
	public UserResponse searchByNickname(String nickname) {
		log.info("searchByNickname : {}", nickname);
		User user = userRepository.findByNicknameAndIsDeletedFalse(nickname)
				.orElseThrow(()-> NotFoundUserNicknameException.EXCEPTION);

		return UserResponse.from(user);
	}

	@Override
	public List<UserResponse> searchByWord(String word) {
		return userRepository.findByWord(word).stream().map(UserResponse::from).toList();
	}

	@Override
	@Transactional
	public List<LevelResponse> addExp(List<ExpRequest> requests) {
		List<LevelResponse> responses = new ArrayList<>();
		List<User> updateUserList = new ArrayList<>();

		for(ExpRequest request : requests) {
			boolean isLevelUp = false;
			User user = readByUserId(request.userId());
			Long totalExp = user.addExp(request.exp());
			log.info("totalExp : {}", totalExp);

			int beforeLevel = user.getLevel();
			int afterLevel = calculateLevel(totalExp);
			log.info("before level : {} after level : {} ", beforeLevel, afterLevel);

			//레벨업 했다면
			if(beforeLevel < afterLevel){
				UserCharacter userCharacter = levelReword(user, afterLevel);
				user.getOwnedCharacters().add(userCharacter);
				user.levelUp(afterLevel);
				isLevelUp = true;
			}

			updateUserList.add(user);
			LevelResponse response = LevelResponse.from(user,isLevelUp);
			responses.add(response);
		}
		userRepository.saveAll(updateUserList);
		return responses;
	}


	@Override
	public UserCharacter levelReword(User user, int characterIndex) {
		Long index = Long.valueOf(characterIndex);
		GameCharacter addGameCharacter = gameCharacterRepository.findById(index)
				.orElseThrow(() -> InvalidCharacterException.EXCEPTION);

		if (userCharacterRepository.existsByUserAndCharacter(user, addGameCharacter)) {
			throw DuplicatedCharacterUserException.EXCEPTION;
		}

		UserCharacter userCharacter = new UserCharacter(user, addGameCharacter);
		return userCharacter;
	}

	/* 공통 component

	 */
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

	//레벨 계산
	private int calculateLevel(Long totalExp){
		if (totalExp >= 2500) {
			return 9;
		} else if (totalExp >= 2000) {
			return 8;
		} else if (totalExp >= 1600) {
			return 7;
		} else if (totalExp >= 1200) {
			return 6;
		} else if (totalExp >= 900) {
			return 5;
		} else if (totalExp >= 600) {
			return 4;
		} else if (totalExp >= 400) {
			return 3;
		} else if (totalExp >= 200) {
			return 2;
		}
		else {
			// 100 미만의 exp는 아직 레벨업이 되지 않은 것으로 처리
			return 1;
		}
	}

	@Override
	public boolean isProfane(String plainText) throws OrtException{
		BadWordResponse response = tokenizer.getTokenizer(plainText);
		if(response == null || response.tokenIds() == null){
			log.error("Tokenizer response is null for text : {}", plainText);
			return true;
		}

		long[] tokenIds = response.tokenIds();
		long[] attentionMask = response.attentionMask();
		long[] inputShape = new long[]{1, tokenIds.length};
		float[][] logits = onnxInference.runInference(tokenIds, attentionMask, inputShape);

		// 모든 로짓에 대해 시그모이드 적용하여 확률 배열 생성
		int numClasses = logits[0].length;
		float[] probabilities = new float[numClasses];
		for (int i = 0; i < numClasses; i++) {
			probabilities[i] = (float)(1 / (1 + Math.exp(-logits[0][i])));
		}

		log.info("Logits: {}", Arrays.toString(logits[0]));
		log.info("Probabilities: {}", Arrays.toString(probabilities));

		boolean[] predictions = new boolean[numClasses];
		for (int i = 0; i < numClasses; i++) {
			predictions[i] = probabilities[i] >= THRESHOLD;
		}
		log.info("Predictions: {}", Arrays.toString(predictions));

		// 예시: "clean"을 제외한 나머지 중 하나라도 true면 욕설이 포함된 것으로 간주
		String[] labelNames = {"여성/가족", "남성", "성소수자", "인종/국적", "연령",
				"지역", "종교", "기타 혐오", "악플/욕설", "clean"};
		for (int i = 0; i < predictions.length; i++) {
			if (predictions[i] && !labelNames[i].equals("clean")) {
				log.info("Predicted Label: {}", labelNames[i]);
				return true;
			}
		}
		return false;
	}
}
