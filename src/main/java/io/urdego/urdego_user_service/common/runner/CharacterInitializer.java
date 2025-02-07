package io.urdego.urdego_user_service.common.runner;

import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CharacterInitializer implements CommandLineRunner {
    private final GameCharacterRepository gameCharacterRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> characterNames = List.of(
                "BASIC","ANGULAR", "BUMPHY", "DOT",
                "PLANET", "SHARP", "SQUARE", "STAR", "WOOL"
        );

        for(String name : characterNames) {
            if(gameCharacterRepository.findByName(name).isEmpty()) {
                GameCharacter gameCharacter = new GameCharacter(name);
                gameCharacterRepository.save(gameCharacter);
                log.info("Created character {} : {}", gameCharacter.getId(), gameCharacter.getName());
            }
        }
    }
}
