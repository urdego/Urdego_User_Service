package io.urdego.urdego_user_service.infra.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.urdego.urdego_user_service.common.properties.AppleOAuthProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AppleClientSecretGenerator {

    private final AppleOAuthProperty property;

    public String generateClientSecret() throws Exception {

        PrivateKey privateKey = getPrivateKey();

        return Jwts.builder()
                .setHeaderParam("kid", property.getKeyId())
                .setIssuer(property.getIss())
                .setAudience(property.getAud())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();

    }

    private PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(property.getKeyPath().replace("classpath:", "src/main/resources/")));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(spec);
    }
}
