package io.urdego.urdego_user_service.auth.config;

import io.urdego.urdego_user_service.auth.jwt.JwtFilter;
import io.urdego.urdego_user_service.auth.service.CustomUserDetailsService;
import io.urdego.urdego_user_service.domain.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CorsConfig corsConfig;
    private final JwtFilter jwtFilter;
	private final CustomUserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
			UserServiceImpl userServiceImpl) throws Exception {

		httpSecurity
				.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
								.requestMatchers("/api/user-service/api-docs/**").permitAll()
								.requestMatchers("/api/user-service/**").permitAll()

				)
				.sessionManagement((sessionManagement) -> sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

}
