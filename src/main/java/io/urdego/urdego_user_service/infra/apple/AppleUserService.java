package io.urdego.urdego_user_service.infra.apple;

import io.jsonwebtoken.Claims;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.constant.PlatfromType;
import io.urdego.urdego_user_service.domain.entity.constant.Role;
import io.urdego.urdego_user_service.domain.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppleUserService {

    private final AppleTokenVerifier appleTokenVerifier;
    private final UserRepository userRepository;

    public User processIdToken(String idToken) throws Exception {
        Claims claims = appleTokenVerifier.verifyIdToken(idToken);

        String platformId = claims.getSubject();
        String nickname = claims.get("name", String.class);
        String email = claims.get("email", String.class);

        Optional<User> existingUser = userRepository.findByPlatformIdAndPlatformType(platformId, PlatfromType.APPLE);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = User.builder()
                .nickname(nickname != null ? nickname : "Apple User")
                .platformId(platformId)
                .platformType(PlatfromType.APPLE)
                .pushAlarm(true)
                .build();

        return userRepository.save(newUser);
    }
}
