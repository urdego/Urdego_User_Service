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
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private final Key key;
	private final long accessExpiration;
	private final long refreshExpiration;

	public JwtUtil(@Value("${jwt.secret-key}") String secretKey,
				   @Value("${jwt.access-expiration-time}") long accessExpiration,
				   @Value("${jwt.refresh-expiration-time}") long refreshExpiration) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessExpiration = accessExpiration;
		this.refreshExpiration = refreshExpiration;
	}

	public TokenRes generateToken(String platformId) {
		long now = System.currentTimeMillis();

		String accessToken = Jwts.builder()
				.setSubject(platformId)
				.setIssuedAt(new Date())
				.setExpiration(new Date(now + accessExpiration))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		String refreshToken = Jwts.builder()
				.setSubject(platformId)
				.setIssuedAt(new Date())
				.setExpiration(new Date(now + refreshExpiration))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();

		return TokenRes.of(accessToken, refreshToken);
	}

	public String getPlatformIdFromToken(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
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