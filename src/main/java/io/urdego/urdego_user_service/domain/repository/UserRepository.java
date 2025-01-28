package io.urdego.urdego_user_service.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.urdego.urdego_user_service.domain.entity.User;

import java.util.List;
import java.util.Optional;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// 닉네임 중복 여부 확인
	boolean existsByNicknameAndIsDeletedFalse(String nickname);

	// 플랫폼 ID와 플랫폼 타입으로 유저 조회
	Optional<User> findByPlatformIdAndPlatformTypeAndIsDeletedFalse(String platformId, PlatformType platformType);

	// 플랫폼 ID와 플랫폼 타입 중복 여부 확인
	boolean existsByPlatformIdAndPlatformTypeAndIsDeletedFalse(String platformId, PlatformType platformType);

	// PK로 삭제되지 않은 유저 조회
	Optional<User> findByIdAndIsDeletedFalse(Long userId);

	boolean existsByEmail(String email);

	Optional<User> findByEmailAndPlatformType(String email, PlatformType platformType);

	// 삭제된 상태의 이메일로 유저 조회
	@Query("SELECT u FROM User u WHERE u.isDeleted = true AND u.email = :email")
	Optional<User> findByEmailAndIsDeletedTrue(@Param("email") String email);

}
