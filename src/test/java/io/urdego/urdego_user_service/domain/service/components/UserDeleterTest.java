package io.urdego.urdego_user_service.domain.service.components;

import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDeleterTest {
    @Mock private UserRepository userRepository;
    @Mock private UserCharacterRepository userCharacterRepository;

    @Mock private UserReader userReader;

    @InjectMocks
    private UserDeleter userDeleter;

    @Test
    void delete_ShouldDeleteUser() {
        //given
        String drwalRequest = "test delete";
        User dummyUser = User.createDummy(1L, "dummy", 1,
                "dummy@gamil.com", "KAKAO", "1");
        GameCharacter dummyGameCharacter = new GameCharacter(1L, "dummyCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        when(userReader.readByUserId(dummyUser.getId())).thenReturn(dummyUser);

        //when
        userDeleter.delete(1L,drwalRequest);

        //then
        verify(userRepository).save(dummyUser);

        verify(userCharacterRepository).deleteByUser(dummyUser);

        assertTrue(dummyUser.getIsDeleted());
        assertEquals(drwalRequest, dummyUser.getWithDrawalReason());
    }
}