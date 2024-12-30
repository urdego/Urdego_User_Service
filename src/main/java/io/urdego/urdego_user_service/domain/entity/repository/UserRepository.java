package io.urdego.urdego_user_service.domain.entity.repository;

import io.urdego.urdego_user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
