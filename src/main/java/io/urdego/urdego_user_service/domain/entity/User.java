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

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@SQLRestriction("is_deleted IS NULL")
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

	private String nickname;

	//미인증, 일반 유저, 관리자, 삭제된 회원
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Column(name = "profile_image")
	private String profileImageUrl;

	// 유저 캐릭터
	@Column(name ="user_character")
	@Enumerated(EnumType.STRING)
	private CharacterType characterType;

	//푸시알림 수신 여부
	@Column(name = "push_alarm")
	private Boolean pushAlarm;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	//탈퇴 이유
	private String withDrawalReason;

	public static User create(UserSignUpRequest signUpRequest) {
		return User.builder()
				.nickname(signUpRequest.nickname())
				.email(signUpRequest.email())
				.platformId(signUpRequest.platformId())
				.platformType(
						PlatformType.valueOf(signUpRequest.platformType().toUpperCase()))
				.role(Role.USER)
				.characterType(CharacterType.DEFAULT)
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

	public void setRoleAndDrwalReason(String withDrawalReason) {
		this.isDeleted = true;
		this.withDrawalReason = withDrawalReason;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void ChangeCharacterType(CharacterType characterType) {
		this.characterType = characterType;
	}

}
