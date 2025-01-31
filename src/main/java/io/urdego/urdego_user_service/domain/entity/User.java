package io.urdego.urdego_user_service.domain.entity;

import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.common.enums.CharacterType;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.api.apple.dto.AppleUserInfoDto;
import io.urdego.urdego_user_service.api.kakao.dto.KakaoUserInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = TRUE WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_Id")
	private Long id;

	@Column(unique = true, name = "platform_id")
	private String platformId;

	//APPLE, KAKAO
	@Column(name = "platform_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PlatformType platformType;

	private String email;

	private String name;

	private String nickname;

	//미인증, 일반 유저, 관리자, 삭제된 회원
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Column(name = "profile_image")
	private String profileImageUrl;

	// 유저 캐릭터
	@Column(name ="character_type")
	@Enumerated(EnumType.STRING)
	private CharacterType characterType;

	//푸시알림 수신 여부
	@Column(name = "push_alarm")
	private Boolean pushAlarm;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	//경험치
	@Column(name = "experience_point")
	private Long exp;

	//탈퇴 이유
	private String withDrawalReason;

	public static User create(UserSignUpRequest signUpRequest, int nicknameNum) {
		return User.builder()
				.name(signUpRequest.nickname())
				.nickname(signUpRequest.nickname() +"#"+nicknameNum)
				.email(signUpRequest.email())
				.platformId(signUpRequest.platformId())
				.platformType(
						PlatformType.valueOf(signUpRequest.platformType().toUpperCase()))
				.role(Role.USER)
				.isDeleted(false)
				.exp(0L)
				.characterType(CharacterType.BASIC)
				.build();
	}

	public static User createAppleUser(AppleUserInfoDto userInfo) {
		return User.builder()
				.nickname(userInfo.getNickname())
				.email(userInfo.getEmail())
				.platformId(userInfo.getPlatformId())
				.platformType(userInfo.getPlatformType())
				.role(Role.USER)
				.profileImageUrl(null)
				.build();
	}

	public static User createKakaoUser(KakaoUserInfoDto userInfo) {
		return User.builder()
				.nickname(userInfo.getKakaoAccount().getProfile().getNickname())
				.email(userInfo.getKakaoAccount().getEmail())
				.profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImage())
				.platformId(userInfo.getId().toString())
				.platformType(userInfo.getType())
				.role(Role.USER)
				.build();
	}

	public void setRoleAndDrawalReason(String withDrawalReason) {
		this.isDeleted = true;
		this.withDrawalReason = withDrawalReason;
		this.platformId = null;
	}

	public void rejoin(String platformId){
		this.isDeleted = false;
		this.withDrawalReason = null;
		this.platformId = platformId;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void ChangeCharacterType(CharacterType characterType) {
		this.characterType = characterType;
	}

	public Long addExp(Long exp) {
		this.exp += exp;
		return exp;
	}
	public void initExp() {
		this.exp = 0L;
	}

}
