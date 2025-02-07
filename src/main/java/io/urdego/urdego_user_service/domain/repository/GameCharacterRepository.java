package io.urdego.urdego_user_service.domain.repository;

import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, Long> {
    Optional<GameCharacter> findByName(String name);

    Optional<GameCharacter> findById(Long id);
}
