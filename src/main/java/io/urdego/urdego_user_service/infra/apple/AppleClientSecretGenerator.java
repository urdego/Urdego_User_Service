package io.urdego.urdego_user_service.infra.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Component
public class AppleClientSecretGenerator {
    @Value("${apple.team-id}")
    private static String TEAM_ID;

    @Value("${apple.aud}")
    private static String CLIENT_ID;

    @Value("${apple.key.id}")
    private static String KEY_ID;

    @Value("${apple.key.path}")
    private static String KEY_PATH;

    public String generateClientSecret() throws Exception {

        PrivateKey privateKey = getPrivateKey();

        return Jwts.builder()
                .setHeaderParam("kid", KEY_ID)
                .setIssuer(TEAM_ID)
                .setSubject(CLIENT_ID)
                .setAudience("https://appleid.apple.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();

    }

    private PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(KEY_PATH.replace("classpath:", "src/main/resources/")));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(spec);
    }
}
