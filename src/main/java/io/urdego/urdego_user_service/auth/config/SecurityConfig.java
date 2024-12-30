/*
package io.urdego.urdego_user_service.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//TODO security 적용 안할 end-point 설정
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/api/user-service/api-docs/**").permitAll()
				)
				.sessionManagement((sessionManagement) ->
						sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore();
	}
}
*/
