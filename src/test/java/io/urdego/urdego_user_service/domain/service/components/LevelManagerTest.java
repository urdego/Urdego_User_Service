package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.common.exception.character.InvalidCharacterException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelManagerTest {
    @Mock private UserReader userReader;
    @Mock private GameCharacterRepository gameCharacterRepository;
    @Mock private UserCharacterRepository userCharacterRepository;

    @InjectMocks
    private LevelManager levelManager;

    @Test
    void calculateLevel_ShouldCalculateLevelThenReturnLevelThreshold(){
        //given
        Long testTotalExp = 200L;

        //when
        int resultLevel = levelManager.calculateLevel(testTotalExp);

        //then
        assertEquals(2,resultLevel);
    }

    @Test
    void calculateLevel_ShouldCalculateLevelThenReturnLevel(){
        //given
        Long testTotalExp = 350L;

        //when
        int resultLevel = levelManager.calculateLevel(testTotalExp);

        //then
        assertEquals(2,resultLevel);
    }

    @Test
    void calculateLevel_ShouldCalculateLevelThenReturnLevelInit(){
        //given
        Long testTotalExp = 0L;

        //when
        int resultLevel = levelManager.calculateLevel(testTotalExp);

        //then
        assertEquals(1,resultLevel);
    }

    @Test
    void levelUpReword_ShouldThrow_InvalidCharacterException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        int characterIndex = 100;

        when(gameCharacterRepository.findById(any(Long.class))).thenThrow(InvalidCharacterException.class);

        //when
        assertThrows(InvalidCharacterException.class, ()-> levelManager.levelUpReword(dummyUser,characterIndex));

        //then
        verify(gameCharacterRepository).findById(any(Long.class));
    }

    @Test
    void levelUpReword_ShouldThrow_DuplicatedCharacterUserException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long characterIndex = 1L;
        GameCharacter dummyAddCharacter = new GameCharacter("dummyCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyAddCharacter);

        when(gameCharacterRepository.findById(characterIndex)).thenReturn(Optional.of(dummyAddCharacter));

        //when
        when(userCharacterRepository.existsByUserAndCharacter(dummyUser,dummyAddCharacter)).thenReturn(true);
        assertThrows(DuplicatedCharacterUserException.class, ()-> levelManager.levelUpReword(dummyUser,1));

        //then
        verify(gameCharacterRepository).findById(any(Long.class));
        verify(userCharacterRepository).existsByUserAndCharacter(any(User.class),any(GameCharacter.class));

    }
}