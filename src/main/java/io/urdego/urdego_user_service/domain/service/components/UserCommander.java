package io.urdego.urdego_user_service.domain.service.components;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private final LevelManager levelManager;

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    // 신규 회원가입
    public User signUp(UserSignUpRequest userSignUpRequest) {
        Long nicknameNumber = countDuplicatedNickname(userSignUpRequest.nickname());
        User newUser = User.create(userSignUpRequest,nicknameNumber);

        UserCharacter userCharacter = userCharacterCommander.initActiveCharacter(newUser);

        userRepository.save(newUser);
        userCharacterRepository.save(userCharacter);

        return newUser;
    }

    // 회원 탈퇴 후 재가입
    public User reSignUp(User existingUser, UserSignUpRequest userSignUpRequest) {
        Long nicknameNumber = countDuplicatedNickname(userSignUpRequest.nickname());
        existingUser.initUserInfo(userSignUpRequest.platformId());
        existingUser.updateNickname(userSignUpRequest.nickname() +"#"+nicknameNumber);
        userCharacterCommander.initActiveCharacter(existingUser);
        save(existingUser);
        return existingUser;
    }

    //닉네임 변경
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
    private Long countDuplicatedNickname(String nickname){
        //List<User> userList = userReader.findByName(nickname);
        //int nicknameNumber = userList.size() + 1;
        Long nicknameNumber = userReader.countByName(nickname);
        return nicknameNumber;
    }

    //경험치 획득
    @Transactional
    public List<LevelResponse> saveExp(List<ExpRequest> requests) {
        List<LevelResponse> responses = new ArrayList<>();
        List<User> updateUserList = new ArrayList<>();

        for(ExpRequest request : requests) {
            boolean isLevelUp = false;
            User user = userReader.readByUserId(request.userId());
            Long totalExp = user.addExp(request.exp());
            log.info("totalExp : {}", totalExp);

            int beforeLevel = user.getLevel();
            int afterLevel = levelManager.calculateLevel(totalExp);
            log.info("before level : {} after level : {} ", beforeLevel, afterLevel);

            //레벨업 했다면
            if(beforeLevel < afterLevel){
                UserCharacter userCharacter = levelManager.levelUpReword(user, afterLevel);
                user.getOwnedCharacters().add(userCharacter);
                user.levelUp(afterLevel);
                isLevelUp = true;
            }

            updateUserList.add(user);
            LevelResponse response = LevelResponse.from(user,isLevelUp);
            responses.add(response);
        }
        saveAll(updateUserList);
        return responses;
    }
}
