package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.common.exception.character.InvalidCharacterException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCharacterReader {
    private final UserCharacterRepository userCharacterRepository;
    private final GameCharacterRepository gameCharacterRepository;

    //GameCharacter
    public GameCharacter readGameCharacterByName(String charactername) {
        return gameCharacterRepository.findByName(charactername)
                .orElseThrow(()-> InvalidCharacterException.EXCEPTION);
    }
}
