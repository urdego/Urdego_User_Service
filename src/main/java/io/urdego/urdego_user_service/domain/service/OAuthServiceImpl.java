package io.urdego.urdego_user_service.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

	@Override
	public String getConnectionUrl() {
		return "";
	}
}
