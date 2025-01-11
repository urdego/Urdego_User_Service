package io.urdego.urdego_user_service.auth.config;

import io.urdego.urdego_user_service.domain.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
			UserServiceImpl userServiceImpl) throws Exception {

		httpSecurity
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/api/user-service/api-docs/**").permitAll()
								.requestMatchers("/api/user-service/**").permitAll()

				)
				.sessionManagement((sessionManagement) ->
						sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
		//httpSecurity.addFilter()
	}


}
