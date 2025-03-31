package io.urdego.urdego_user_service.domain.service.components;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
import io.urdego.urdego_user_service.common.exception.user.ReLoginFailException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCommander {
    private final UserRepository userRepository;
    private final GameCharacterRepository gameCharacterRepository;
    private final UserCharacterRepository userCharacterRepository;

    private final UserReader userReader;
    private final UserValidator userValidator;
    private final UserCharacterCommander userCharacterCommander;

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    // 신규 회원가입
    public User signUp(UserSignUpRequest userSignUpRequest) {
        int nicknameNumber = countDuplicatedNickname(userSignUpRequest.nickname());
        User newUser = User.create(userSignUpRequest,nicknameNumber);

        UserCharacter userCharacter = userCharacterCommander.initActiveCharacter(newUser);

        userRepository.save(newUser);
        userCharacterRepository.save(userCharacter);

        return newUser;
    }

    // 회원 탈퇴 후 재가입
    public User reSignUp(User existingUser, UserSignUpRequest userSignUpRequest) {
        int nicknameNumber = countDuplicatedNickname(userSignUpRequest.nickname());
        existingUser.initUserInfo(userSignUpRequest.platformId());
        existingUser.updateNickname(userSignUpRequest.nickname() +"#"+nicknameNumber);
        userCharacterCommander.initActiveCharacter(existingUser);
        save(existingUser);
        return existingUser;
    }

    public User updateNickname(Long userId, String newNickname) throws OrtException {
        User user = userReader.readByUserId(userId);
        userValidator.validateNickname(newNickname);

        log.info("new nickname : {}", newNickname);
        log.info("real nickname : {}", user.getNickname());
        user.updateNickname(newNickname);
        save(user);
        return user;
    }

    // 회원가입 시 닉네임 넘버링
    private int countDuplicatedNickname(String nickname){
        List<User> userList = userReader.findByName(nickname);
        int nicknameNumber = userList.size() + 1;
        return nicknameNumber;
    }
}
