package io.urdego.urdego_user_service.auth.service.apple;

import io.urdego.urdego_user_service.auth.jwt.JwtTokenProvider;
import io.urdego.urdego_user_service.auth.service.OAuthService;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.dto.AppleUserInfoDto;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import io.urdego.urdego_user_service.infra.apple.AppleTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppleAuthServiceImpl implements AppleAuthService {

    private final AppleTokenVerifier appleTokenVerifier;
    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String appleLogin(String idToken) throws Exception {
        // 사용자 정보 가져오기
        AppleUserInfoDto userInfo = oAuthService.getAppleOAuthProfile(idToken);

        System.out.println("OAuthProfile: " + userInfo.getEmail());

        Optional<User> existingUser = userRepository.findByPlatformIdAndPlatformType(userInfo.getPlatformId(), userInfo.getPlatformType());
        if (existingUser.isEmpty()) {
            User newUser = User.createAppleUser(userInfo);
            return jwtTokenProvider.generateToken(newUser.getEmail());
        }

        return jwtTokenProvider.generateToken(userInfo.getEmail());
    }
}
