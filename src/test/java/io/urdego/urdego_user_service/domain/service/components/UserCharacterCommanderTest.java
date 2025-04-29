package io.urdego.urdego_user_service.domain.service.components;

import com.google.common.cache.CacheLoader;
import io.urdego.urdego_user_service.api.user.dto.request.ChangeCharacterRequest;
import io.urdego.urdego_user_service.common.exception.user.InvalidActiveCharacterException;
import io.urdego.urdego_user_service.common.exception.user.ReLoginFailException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.NotFoundCharacterException;
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
class UserCharacterCommanderTest {
    @Mock private UserCharacterRepository userCharacterRepository;
    @Mock private GameCharacterRepository gameCharacterRepository;
    @Mock private UserReader userReader;
    @Mock private UserCharacterReader userCharacterReader;

    @InjectMocks UserCharacterCommander userCharacterCommander;

    @Test
    void initActiveCharacter_ShouldDeletedUserReLogin_ThenReturnReLoginFailException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        GameCharacter dummyGameCharacter = new GameCharacter("test");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);

        when(userCharacterRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        //when
        assertThrows(ReLoginFailException.class, ()-> userCharacterCommander.initActiveCharacter(dummyUser));

        //then
        verify(userCharacterRepository).findByUser(any(User.class));
    }

    @Test
    void initActiveCharacter_ShouldDeletedUserReLogin_ThenReturnExistUser(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        GameCharacter dummyGameCharacter = new GameCharacter("test");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);

        when(userCharacterRepository.findByUser(dummyUser)).thenReturn(Optional.of(dummyUserCharacter));

        //when
        UserCharacter result = userCharacterCommander.initActiveCharacter(dummyUser);

        //then
        assertEquals(dummyUserCharacter, result);
        verify(userCharacterRepository).findByUser(any(User.class));
    }

    @Test
    void initActiveCharacter_ShouldAddBasicCharacter_WhenFirstLogin(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        GameCharacter basicCharacter = new GameCharacter("BASIC");
        when(gameCharacterRepository.findById(1L)).thenReturn(Optional.of(basicCharacter));
        UserCharacter userCharacter = new UserCharacter(dummyUser,basicCharacter);

        //when
        UserCharacter result = userCharacterCommander.initActiveCharacter(dummyUser);

        //then
        assertEquals(basicCharacter, dummyUser.getActiveCharacter());
        assertEquals(1, dummyUser.getOwnedCharacters().size());
        assertEquals(basicCharacter,result.getCharacter());
    }

    @Test
    void updateActiveCharacter_ShouldChangeActiveCharacter_ThenThrowNotFoundCharacterException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long userId = dummyUser.getId();
        ChangeCharacterRequest characterRequest = new ChangeCharacterRequest("testCharacter");
        GameCharacter dummyGameCharacter = new GameCharacter(userId, "testCharacter");
        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userCharacterReader.readGameCharacterByName(characterRequest.characterName()))
                .thenReturn(dummyGameCharacter);
        //when
        assertThrows(NotFoundCharacterException.class,()-> userCharacterCommander.updateActiveCharacter(userId,characterRequest));
        verify(userReader).readByUserId(userId);
        verify(userCharacterReader).readGameCharacterByName(characterRequest.characterName());
    }

    @Test
    void updateActiveCharacter_ShouldChangeActiveCharacter_ThenThrowInvalidActiveCharacterException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long userId = dummyUser.getId();

        //기존 캐릭터 세팅
        GameCharacter dummyGameCharacter = new GameCharacter("testCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);
        dummyUser.changeActiveCharacter(dummyGameCharacter);

        ChangeCharacterRequest characterRequest = new ChangeCharacterRequest("testCharacter");

        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userCharacterReader.readGameCharacterByName(characterRequest.characterName()))
                .thenReturn(dummyGameCharacter);

        //when
        //then
        assertThrows(InvalidActiveCharacterException.class, () -> userCharacterCommander.updateActiveCharacter(userId,characterRequest));
        verify(userReader).readByUserId(userId);
        verify(userCharacterReader).readGameCharacterByName(characterRequest.characterName());
    }

    @Test
    void updateActiveCharacter_ShouldChangeActiveCharacter_ThenChangedActiveCharacter(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long userId = dummyUser.getId();

        //기존 캐릭터 세팅
        GameCharacter dummyGameCharacter = new GameCharacter("testCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);
        dummyUser.changeActiveCharacter(dummyGameCharacter);

        //바꿀 캐릭터
        ChangeCharacterRequest characterRequest = new ChangeCharacterRequest("newTestCharacter");
        GameCharacter newDummyGameCharacter = new GameCharacter(userId, "newTestCharacter");
        UserCharacter newDummyUserCharacter = new UserCharacter(dummyUser, newDummyGameCharacter);
        dummyUser.getOwnedCharacters().add(newDummyUserCharacter);

        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userCharacterReader.readGameCharacterByName(characterRequest.characterName()))
                .thenReturn(newDummyGameCharacter);

        //when
        User result = userCharacterCommander.updateActiveCharacter(userId,characterRequest);

        //then
        assertEquals(dummyUser.getActiveCharacter(),result.getActiveCharacter());
        verify(userReader).readByUserId(userId);
        verify(userCharacterReader).readGameCharacterByName(characterRequest.characterName());
    }

    @Test
    void addCharacter_ShouldAddDuplicatedCharacter_ThenThrowDuplicatedCharacterUserException(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long userId = dummyUser.getId();
        ChangeCharacterRequest characterRequest = new ChangeCharacterRequest("testCharacter");

        //기본 유저 캐릭터 설정
        GameCharacter dummyGameCharacter = new GameCharacter("testCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);
        dummyUser.changeActiveCharacter(dummyGameCharacter);

        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userCharacterReader.readGameCharacterByName(characterRequest.characterName())).thenReturn(dummyGameCharacter);

        when(userCharacterRepository.existsByUserAndCharacter(dummyUser, dummyGameCharacter)).thenReturn(true);

        //when
        //then
        assertThrows(DuplicatedCharacterUserException.class, ()-> userCharacterCommander.addCharacter(userId,characterRequest));
        verify(userReader).readByUserId(userId);
        verify(userCharacterReader).readGameCharacterByName(characterRequest.characterName());
    }

    @Test
    void addCharacter_ShouldAddCharacter_ThenReturnUser(){
        //given
        User dummyUser = User.createDummy(1L,"dummyNickname", 1, "email@email.com", "KAKAO", "1");
        Long userId = dummyUser.getId();
        ChangeCharacterRequest characterRequest = new ChangeCharacterRequest("newTestCharacter");

        //기본 유저 캐릭터 설정
        GameCharacter dummyGameCharacter = new GameCharacter("testCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        dummyUser.getOwnedCharacters().add(dummyUserCharacter);
        dummyUser.changeActiveCharacter(dummyGameCharacter);

        //새로 추가될 캐릭터
        GameCharacter newDummyGameCharacter = new GameCharacter(characterRequest.characterName());

        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userCharacterReader.readGameCharacterByName(characterRequest.characterName())).thenReturn(newDummyGameCharacter);

        when(userCharacterRepository.existsByUserAndCharacter(dummyUser,newDummyGameCharacter)).thenReturn(false);

        //when
        User result = userCharacterCommander.addCharacter(userId, characterRequest);

        //then
        assertEquals(2, result.getOwnedCharacters().size());
        assertEquals(characterRequest.characterName(),result.getOwnedCharacters().get(1).getCharacter().getName());
        verify(userReader).readByUserId(userId);
        verify(userCharacterReader).readGameCharacterByName(characterRequest.characterName());
    }
}