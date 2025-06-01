package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.api.user.dto.response.UserSimpleResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.exception.user.NotFoundUserException;
import io.urdego.urdego_user_service.common.exception.user.NotFoundUserNicknameException;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserReader {
    private final UserRepository userRepository;

  /*  public List<User> findByName(String nickname){
        return userRepository.findByName(nickname);
    }*/

    public Long countByName(String nickname){
        return userRepository.countByName(nickname);
    }

    public User readByUserId(Long userId) {
      return userRepository.findByIdAndIsDeletedFalse(userId)
              .orElseThrow(()-> NotFoundUserException.EXCEPTION);
    }

    public User findByEmailAndPlatformType(String email, PlatformType platformType) {
        return userRepository.findByEmailAndPlatformType(email, platformType)
                .orElseThrow(()-> NotFoundUserException.EXCEPTION);
    }

    public List<User> findByWord(String word){
        return userRepository.findByWord(word);
    }

    public User findByNicknameAndIsDeletedFalse(String nickname) {
        log.info("searchByNickname : {}", nickname);
        return userRepository.findByNicknameAndIsDeletedFalse(nickname)
                .orElseThrow(()-> NotFoundUserNicknameException.EXCEPTION);
    }

    public List<UserSimpleResponse> readAlltoList(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        List<UserSimpleResponse> responses = new ArrayList<>();
        for(User user : users) {
            UserSimpleResponse response = UserSimpleResponse.from(user);
            responses.add(response);
        }
        return responses;
    }

    // boolean
    public boolean existsByEmailAndPlatformType(String email, PlatformType platformType) {
        return userRepository.existsByEmailAndPlatformType(email, platformType);
    }

    public boolean existsByNicknameAndIsDeletedFalse(String nickname) {
        return userRepository.existsByNicknameAndIsDeletedFalse(nickname);
    }
}
