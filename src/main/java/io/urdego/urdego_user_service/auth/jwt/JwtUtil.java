package io.urdego.urdego_user_service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	private final Key key;

	public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenRes generateToken(String email) {
		long now = System.currentTimeMillis();
		long expiration = 1000 * 60 * 60;

		String accessToken = Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + expiration))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | ExpiredJwtException |
		         UnsupportedJwtException | IllegalArgumentException e) {
			throw e;
		}
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}