package io.urdego.urdego_user_service.domain.entity;

import io.urdego.urdego_user_service.api.user.dto.request.UserSignUpRequest;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

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

	//푸시알림 수신 여부
	@Column(name = "push_alarm")
	private Boolean pushAlarm;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;

	//경험치
	@Column(name = "experience_point")
	private Long exp;

	//탈퇴 이유
	private String withDrawalReason;

	//보유 캐릭터 현황
	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "user",
			cascade = CascadeType.ALL
	)
	private List<UserCharacter> ownedCharacters = new ArrayList<>();

	//현재 사용 중인 캐릭터
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "active_character_id",nullable = true)
	private GameCharacter activeCharacter;

	public static User create(UserSignUpRequest signUpRequest, int nicknameNum) {
		return User.builder()
				.name(signUpRequest.nickname())
				.nickname(signUpRequest.nickname() +"#"+nicknameNum)
				.email(signUpRequest.email())
				.platformId(signUpRequest.platformId())
				.platformType(
						PlatformType.valueOf(signUpRequest.platformType().toUpperCase()))
				.ownedCharacters(new ArrayList<>())
				.role(Role.USER)
				.isDeleted(false)
				.exp(0L)
				.build();
	}

	public void setRoleAndDrawalReason(String withDrawalReason) {
		this.isDeleted = true;
		this.withDrawalReason = withDrawalReason;
		this.platformId = null;
	}

	public void initUserInfo(String platformId){
		this.isDeleted = false;
		this.withDrawalReason = null;
		this.platformId = platformId;
		this.ownedCharacters = new ArrayList<>();
		this.exp = 0L;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long addExp(Long exp) {
		this.exp += exp;
		return exp;
	}


	public void changeActiveCharacter(GameCharacter character) {
		this.activeCharacter = character;
	}
	public void addCharacter(UserCharacter character) {
		this.ownedCharacters.add(character);
	}

}
