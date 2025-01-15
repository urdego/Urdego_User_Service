/*
package io.urdego.urdego_user_service.auth.service;

import io.urdego.urdego_user_service.common.enums.Role;
import io.urdego.urdego_user_service.domain.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private User user;

	private GrantedAuthority getAuthority(Role role) {
		return new SimpleGrantedAuthority("ROLE_" + role.name());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

		switch (user.getRole()) {
			case UNAUTH : authorities.add(getAuthority(Role.UNAUTH));
			case USER : authorities.add(getAuthority(Role.USER));
			case ADMIN : authorities.add(getAuthority(Role.ADMIN));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	// 넘버링에 따라 결정
	@Override
	public String getUsername() {
		return user.getPlatformId();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getNickname() {
		return user.getNickname();
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return !user.getRole().equals(Role.DELETED);
	}
}
*/
