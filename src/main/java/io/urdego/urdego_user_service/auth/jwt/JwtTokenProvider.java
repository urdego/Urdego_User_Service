package io.urdego.urdego_user_service.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(String email) {
		long now = System.currentTimeMillis();
		long expiration = 1000 * 60 * 60;

		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + expiration))
				.signWith(key)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getEmailFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}