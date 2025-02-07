package io.urdego.urdego_user_service.domain.repository;

import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    Optional<UserCharacter> findByUser(User user);
    boolean existsByUserAndCharacter(User user, GameCharacter character);
}
