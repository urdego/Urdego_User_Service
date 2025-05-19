package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.common.exception.character.InvalidCharacterException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LevelManager {
    private final UserReader userReader;
    private final GameCharacterRepository gameCharacterRepository;
    private final UserCharacterRepository userCharacterRepository;

    //레벨 업 보상 지급
    public UserCharacter levelUpReword(User user, int characterIndex) {
        Long index = Long.valueOf(characterIndex);
        GameCharacter addGameCharacter = gameCharacterRepository.findById(index)
                .orElseThrow(() -> InvalidCharacterException.EXCEPTION);

        if (userCharacterRepository.existsByUserAndCharacter(user, addGameCharacter)) {
            throw DuplicatedCharacterUserException.EXCEPTION;
        }

        UserCharacter userCharacter = new UserCharacter(user, addGameCharacter);
        return userCharacter;
    }

    public int calculateLevel(Long totalExp){
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
}
