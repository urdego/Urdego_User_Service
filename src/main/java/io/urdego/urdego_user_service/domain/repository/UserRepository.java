package io.urdego.urdego_user_service.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.urdego.urdego_user_service.api.user.dto.response.UserResponse;
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

	// PK로 삭제되지 않은 유저 조회
	Optional<User> findByIdAndIsDeletedFalse(Long userId);

	boolean existsByEmailAndPlatformType(String email, PlatformType platformType);

	Optional<User> findByEmailAndPlatformType(String email, PlatformType platformType);

	//이름으로 검색한 회원 리스트
	List<User> findByName(String name);


    Optional<User> findByNicknameAndIsDeletedFalse(String nickname);

	@Query("SELECT u from User u where u.nickname like %:word%")
	List<User> findByWord(String word);
}
