package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.api.user.dto.response.UserCharacterResponse;
import io.urdego.urdego_user_service.common.exception.user.InvalidActiveCharacterException;
import io.urdego.urdego_user_service.common.exception.user.ReLoginFailException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.NotFoundCharacterException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCharacterCommander {
    private final UserCharacterRepository userCharacterRepository;
    private final GameCharacterRepository gameCharacterRepository;

    private final UserReader userReader;

    private final UserCharacterReader userCharacterReader;

    //기본 캐릭터로 초기화
    public UserCharacter initActiveCharacter(User user){
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

    public User updateActiveCharacter(
            Long userId, ChangeCharacterRequest request) {
        User user = userReader.readByUserId(userId);
        GameCharacter changeCharacter = userCharacterReader.readGameCharacterByName(request.characterName());
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
                return user;
            }
        }
        throw NotFoundCharacterException.EXCEPTION;
    }

    public User addCharacter(Long userId, ChangeCharacterRequest request) {
        User user = userReader.readByUserId(userId);
        GameCharacter addGameCharacter = userCharacterReader.readGameCharacterByName(request.characterName());

        log.info("characterId : {}",addGameCharacter.getId());

        if(userCharacterRepository.existsByUserAndCharacter(user, addGameCharacter)){
            throw DuplicatedCharacterUserException.EXCEPTION;
        }

        UserCharacter userCharacter = new UserCharacter(user, addGameCharacter);
        user.addCharacter(userCharacter);

        return user;
    }
}
