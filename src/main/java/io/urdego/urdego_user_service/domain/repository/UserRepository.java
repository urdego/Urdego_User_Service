package io.urdego.urdego_user_service.domain.repository;

import io.urdego.urdego_user_service.domain.entity.User;
import java.util.Optional;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);
	Optional<User> findByPlatformId(String platformId);

	boolean existsByEmail(String Email);
    Optional<User> findByPlatformIdAndPlatformType(String platformId, PlatformType platformType);
}