package io.urdego.urdego_user_service.domain.entity.repository;

import io.urdego.urdego_user_service.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);
	Optional<User> findByPlatformId(String platformId);

	boolean existsByEmail(String Email);
}
