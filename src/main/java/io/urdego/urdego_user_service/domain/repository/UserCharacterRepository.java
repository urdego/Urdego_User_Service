package io.urdego.urdego_user_service.domain.repository;

import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.entity.UserCharacterPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, UserCharacterPK> {
    Optional<UserCharacter> findByUser(User user);
    Optional<UserCharacter> findByUserAndCharacter(User user,GameCharacter gameCharacter);
    boolean existsByUserAndCharacter(User user, GameCharacter character);
}
