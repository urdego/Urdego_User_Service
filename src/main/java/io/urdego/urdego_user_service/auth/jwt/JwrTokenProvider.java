package io.urdego.urdego_user_service.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.urdego.urdego_user_service.auth.Tokens;
import io.urdego.urdego_user_service.auth.UserPrincipal;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwrTokenProvider {
	private static long ACCESS_EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
	private static long REFRESH_EXPIRATION_TIME = ACCESS_EXPIRATION_TIME * 24; // 하루

	@Value("${jwt.secret-key}")
	private static String SECRET_KEY;

	public JwrTokenProvider(
			@Value("${jwt.secret-key}") String secretKey,
			@Value("${jwt.refresh-expiration-time}") long refreshExpirationTime,
			@Value("${jwt.access-expiration-time}")long accessExpirationTime
	){
		ACCESS_EXPIRATION_TIME = accessExpirationTime;
		REFRESH_EXPIRATION_TIME = refreshExpirationTime;
		SECRET_KEY = secretKey;
	}

	private static Key getKey(){
		return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	}

	public Tokens generate(UserPrincipal principal) {
		return Tokens.builder()
				.accessToken(generateAccessToken(principal))
				.refreshToken(generateRefreshToken(principal))
				.build();
	}

	private static String generateAccessToken(UserPrincipal principal) {
		return Jwts.builder()
				.setHeader(setHeader("ACCESS"))
				.setClaims(setClaims(principal))
				.setSubject(String.valueOf(principal.userId()))
				.setIssuedAt(getNowDate())
				.setExpiration(new Date(getNowDate().getTime() + ACCESS_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, getKey())
				.compact();
	}

	private static String generateRefreshToken(UserPrincipal principal) {
		return Jwts.builder()
				.setHeader(setHeader("REFRESH"))
				.setClaims(setClaims(principal))
				.setSubject(String.valueOf(principal.userId()))
				.setIssuedAt(getNowDate())
				.setExpiration(new Date(getNowDate().getTime() + REFRESH_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, getKey())
				.compact();
	}

	private static Map<String, Object> setHeader(String type){
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", "JWT");
		headers.put("tokenType", type);
		headers.put("alg", "HS256");
		return headers;
	}

	private static Map<String, Object> setClaims(UserPrincipal principal){
		Map<String, Object> claims = new HashMap<>();
		claims.put("ROLE",principal.role().getRole());
		return claims;
	}

	private static Date getNowDate(){return new Date();}
}
