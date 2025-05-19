package io.urdego.urdego_user_service.domain.service.components;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.BadWordResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.exception.user.InvalidNicknameUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.infra.model.OnnxInference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    @Mock private UserReader userReader;
    @Mock private Tokenizer tokenizer;
    @Mock private OnnxInference onnxInference;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void checkSignUpUser_shouldReturnTrue() {
        //given
        String email = "test@gmail.com";
        PlatformType platformType = PlatformType.KAKAO;

        //when
        when(userReader.existsByEmailAndPlatformType(email, platformType)).thenReturn(true);
        boolean result = userValidator.checkSignUpUser(email, platformType);

        //then
        verify(userReader, times(1)).existsByEmailAndPlatformType(email, platformType);
        assertTrue(result);
    }

    @Test
    void checkSignUpUser_shouldReturnFalse() {
        //given
        String email = "test@gmail.com";
        PlatformType platformType = PlatformType.KAKAO;


        //when
        when(userReader.existsByEmailAndPlatformType(email, platformType)).thenReturn(false);
        boolean result = userValidator.checkSignUpUser(email, platformType);

        //then
        verify(userReader, times(1)).existsByEmailAndPlatformType(email, platformType);
        assertFalse(result);
    }

    @Test
    void validateNickname_ShouldValidateDuplicatedNicknameThenThrowException() throws OrtException {
        //given
        String nickname = "DuplicatedNickname";

        //when
        when(userReader.existsByNicknameAndIsDeletedFalse(nickname)).thenReturn(true);

        //then
        assertThrows(DuplicatedCharacterUserException.class, () -> userValidator.validateNickname(nickname));
        verify(userReader, times(1)).existsByNicknameAndIsDeletedFalse(nickname);
    }

    @Test
    void validateNickname_ShouldValidateInvalidNicknameThenThrowException() throws OrtException {
        //given
        String nickname = "BadNickname";
        when(userReader.existsByNicknameAndIsDeletedFalse(nickname)).thenReturn(false);

        //when
        when(tokenizer.getTokenizer(anyString()))
                .thenReturn(new BadWordResponse(
                        new long[]{1L, 2L, 3L},    // dummy tokenIds
                        new String[]{"Bad", "Nick", "name"}, // dummy tokens
                        new long[]{1L, 1L, 1L}     // dummy attentionMask
                ));
        when(onnxInference.runInference(any(),any(), any())).thenReturn(new float[][]{{10.0f, -5.0f}});

        //then
        assertThrows(InvalidNicknameUserException.class, () -> userValidator.validateNickname(nickname));
        verify(tokenizer, times(1)).getTokenizer(nickname);
        verify(onnxInference, times(1)).runInference(any(),any(), any());
    }

    @Test
    void validateNickname_ShouldReturnTrue() throws OrtException {
        //given
        String nickname = "GoodNickname";
        when(userReader.existsByNicknameAndIsDeletedFalse(nickname)).thenReturn(false);

        //when
        when(tokenizer.getTokenizer(anyString()))
                .thenReturn(new BadWordResponse(
                        new long[]{1L, 2L, 3L},
                        new String[]{"Good", "Nick", "name"},
                        new long[]{1L, 1L, 1L}
                ));
        when(onnxInference.runInference(any(), any(), any()))
                .thenReturn(new float[][]{
                        { -5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f, -5.0f, 10.0f }
                });
        boolean result = userValidator.validateNickname(nickname);

        //then
        assertTrue(result);
        verify(tokenizer, times(1)).getTokenizer(nickname);
        verify(onnxInference, times(1)).runInference(any(),any(), any());
    }

    @Test
    void checkDeleteUser_ShouldReturnTrue(){
        User dummyUser = User.createDummy(1L,"dummyUser1", 1,
                "test1@gmail.com", "KAKAO", "1");
        dummyUser.setIsDeleted("testDelete");

        boolean result = userValidator.checkDeletedUser(dummyUser);

        assertTrue(result);
        assertEquals(true, dummyUser.getIsDeleted());
        assertEquals("testDelete", dummyUser.getWithDrawalReason());
    }

    @Test
    void checkDeleteUser_ShouldReturnFalse(){
        User dummyUser = User.createDummy(1L,"dummyUser1", 1,
                "test1@gmail.com", "KAKAO", "1");

        boolean result = userValidator.checkDeletedUser(dummyUser);

        assertFalse(result);
        assertEquals(false, dummyUser.getIsDeleted());
    }

    @Test
    void isProfane_ShouldGetTokenizer_ThenReturnNull() throws OrtException {
        //when
        when(tokenizer.getTokenizer(anyString()))
                .thenReturn(null);

        boolean result = userValidator.isProfane(anyString());

        assertTrue(result);
    }
}