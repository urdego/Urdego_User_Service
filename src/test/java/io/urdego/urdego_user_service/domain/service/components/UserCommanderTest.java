package io.urdego.urdego_user_service.domain.service.components;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.ExpRequest;
import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.api.user.dto.response.LevelResponse;
import io.urdego.urdego_user_service.domain.entity.GameCharacter;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.domain.entity.UserCharacter;
import io.urdego.urdego_user_service.domain.repository.GameCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserCharacterRepository;
import io.urdego.urdego_user_service.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommanderTest {
    @Mock private UserRepository userRepository;
    @Mock private GameCharacterRepository gameCharacterRepository;
    @Mock private UserCharacterRepository userCharacterRepository;

    @Mock private UserReader userReader;
    @Mock private UserValidator userValidator;
    @Mock private UserCharacterCommander userCharacterCommander;
    @Mock private LevelManager levelManager;

    @InjectMocks
    private UserCommander userCommander;

    /*
    * 1. 중복된 닉네임이 몇개 있는지 체크
    * 2. 유저 생성
    * 3. 캐릭터 초기화 (기본 캐릭터 지급)
    * 4. 상태 저장
    * 5. 저장된 캐릭터 정보 리턴
    * */
    @Test
    void signUp_ShouldCreateUserWithCharacter(){
        //given
        UserSignUpRequest request = new UserSignUpRequest("nickname","email@email.com","KAKAO","1");
        List<User> userList = List.of(mock(User.class), mock(User.class));

        //1. 중복된 닉네임이 몇개 있는지 체크
        when(userReader.findByName(request.nickname())).thenReturn(userList);
        int nicknameNumber = userList.size() + 1;

        //2. 유저 생성
        User dummyUser = User.create(request,nicknameNumber);

        //3. 캐릭터 초기화 (기본 캐릭터 지급)
        //여기서 분기가 등장 최초 가입 시 or 탈퇴 후 재 로그인 시
        //현재는 최초 가입 시로 가정
        GameCharacter dummyGameCharacter = new GameCharacter("dummyGameCharacter");
        UserCharacter dummyUserCharacter = new UserCharacter(dummyUser,dummyGameCharacter);
        when(userCharacterCommander.initActiveCharacter(any(User.class))).thenReturn(dummyUserCharacter);

        //when
        //4. 상태 저장
        when(userRepository.save(any(User.class))).thenReturn(dummyUser);
        when(userCharacterRepository.save(any(UserCharacter.class))).thenReturn(dummyUserCharacter);

        //then
        //5 검증 및 확인
        User result = userCommander.signUp(request);

        //5.1 중복된 닉네임이 몇개 있는지 체크
        //검증
        verify(userReader).findByName(request.nickname());
        //확인
        assertEquals("nickname#3",result.getNickname());

        //5.2 유저 생성
        //검증
        verify(userRepository,times(1)).save(any(User.class));
        //확인
        assertEquals(dummyUser.getNickname(), result.getNickname());
        assertEquals(dummyUser.getEmail(), result.getEmail());

        //5.3 캐릭터 초기화 (기본 캐릭터 지급)
        //검증, 확인
        verify(userCharacterCommander).initActiveCharacter(any(User.class));

        //5.4 상태 저장
        //검증
        verify(userRepository,times(1)).save(any(User.class));
        verify(userCharacterRepository,times(1)).save(any(UserCharacter.class));
        //확인
        assertEquals(dummyUser.getNickname(), result.getNickname());
        assertEquals(dummyUser.getEmail(), result.getEmail());

    }

    /*
    1. 중복 닉네임 수 체크
    2. 유저의 정보 초기화
    3. 중복 닉네임 수 체크된 닉네임으로 변경
    4. 사용중인 캐릭터 초기화
    5. 저장
    * */
    @Test
    void reSignUp_ShouldInitializeUserWithCharacter(){
        //given
        UserSignUpRequest request = new UserSignUpRequest("nickname","email@email.com","KAKAO","1");
        List<User> userList = List.of(mock(User.class), mock(User.class));

        //1. 중복 닉네임 수 체크
        when(userReader.findByName(request.nickname())).thenReturn(userList);
        int nicknameNumber = userList.size() + 1;

        //탈퇴된 더미 유저 생성
        User dummyDeletedUser = User.create(request,nicknameNumber);
        dummyDeletedUser.setIsDeleted("Deleted Test");

        //when
        //2. 유저의 정보 초기화
        User result = userCommander.reSignUp(dummyDeletedUser,request);

        //then
        //1. 중복 닉네임 수 검증
        verify(userReader).findByName(request.nickname());
        assertEquals("nickname#3",result.getNickname());

        //2. 탈퇴된 더미 유저의 상태가 바뀌었는지( 정상 유저로 초기화 됐는지) 검증 및 확인
        assertEquals(false,result.getIsDeleted());
        assertEquals(null,result.getWithDrawalReason());
        assertEquals(dummyDeletedUser.getPlatformId(), result.getPlatformId());
        assertEquals(0,result.getExp());
        assertEquals(0, result.getOwnedCharacters().size());

        //캐릭터 초기화 호출 검증
        verify(userCharacterCommander,times(1)).initActiveCharacter(any(User.class));

        //저장됐는지 검증
        verify(userRepository,times(1)).save(any(User.class));
    }

    /*
    1. userId로 해당 유저 검색
    2. newNickName이 올바른 닉네임인지 검증
    3. 새로운 닉네임 저장
    */
    @Test
    void updateNickname_ShouldUpdateUserNickname() throws OrtException {
// given
        Long userId = 1L;
        String oldNickname = "dummyNickname";
        String newNickname = "newNickname";

        User dummyUser = User.createDummy(1L,oldNickname, 1, "email@email.com", "KAKAO", "1");

        when(userReader.readByUserId(userId)).thenReturn(dummyUser);
        when(userValidator.validateNickname(newNickname)).thenReturn(true);

        // when
        User result = userCommander.updateNickname(userId, newNickname);

        // then
        verify(userReader).readByUserId(userId);
        verify(userValidator).validateNickname(newNickname);
        verify(userRepository).save(dummyUser);

        assertEquals(newNickname, result.getNickname());
    }
    /*
        1. Request에 있는 유저 리스트 검색(UserId)
        2. user 에게 exp 추가
        3. 유저 레벨업 여부 확인
    * */
    @Test
    void saveExp_ShouldSaveExp(){
        //given
        ExpRequest dummyRequest1 = new ExpRequest(1L,50L);
        ExpRequest dummyRequest2 = new ExpRequest(2L,100L);
        ExpRequest dummyRequest3 = new ExpRequest(3L,200L);
        User dummyUser1 = User.createDummy(1L,"dummyUser1", 1,
                "test1@gmail.com", "KAKAO", "1");

        User dummyUser2 = User.createDummy(2L,"dummyUser2", 1,
                "test2@gmail.com", "KAKAO", "1");

        User dummyUser3 = User.createDummy(3L,"dummyUser3", 1,
                "test3@gmail.com", "KAKAO", "1");

        List<ExpRequest> requests = List.of(dummyRequest1, dummyRequest2, dummyRequest3);

        when(userReader.readByUserId(1L)).thenReturn(dummyUser1);
        when(userReader.readByUserId(2L)).thenReturn(dummyUser2);
        when(userReader.readByUserId(3L)).thenReturn(dummyUser3);

        //when
        List<LevelResponse> responses = userCommander.saveExp(requests);

        //then
        verify(userReader).readByUserId(1L);
        verify(userReader).readByUserId(2L);
        verify(userReader).readByUserId(3L);

        verify(userRepository).saveAll(List.of(dummyUser1, dummyUser2, dummyUser3));

        assertEquals(50L,dummyUser1.getExp());
        assertEquals(100L,dummyUser2.getExp());
        assertEquals(200L,dummyUser3.getExp());
    }
}