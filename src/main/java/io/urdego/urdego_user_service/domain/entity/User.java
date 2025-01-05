package io.urdego.urdego_user_service.domain.entity;

import io.urdego.urdego_user_service.domain.entity.constant.PlatfromType;
import io.urdego.urdego_user_service.domain.entity.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
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
	private PlatfromType platformType;

	private String email;

	private String nickname;

	//미인증, 일반 유저, 관리자, 삭제된 회원
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Column(name = "profile_image")
	private String profileImageUrl;

	//푸시알림 수신 여부
	@Column(name = "push_alarm")
	private Boolean pushAlarm;

	//탈퇴 이유
	private String withDrawalReason;

	@Builder
	public User(String nickname, String email, String profileImageUrl, Long platformId){

		this.nickname = nickname;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.platformId = platformId.toString();
		this.role = Role.USER;
		this.platformType = PlatfromType.KAKAO;
	}

}
