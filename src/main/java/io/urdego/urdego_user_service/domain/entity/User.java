package io.urdego.urdego_user_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "platform_type")
	private PlatfromType platformType;

	private String nickname;

	private Role role;

	@Column(name = "profile_image_uri")
	private String profileImageUrl;

	@Column(name = "push_alarm")
	private Boolean pushAlarm;

	//탈퇴 이유
	private String withDrawalReason;
}
