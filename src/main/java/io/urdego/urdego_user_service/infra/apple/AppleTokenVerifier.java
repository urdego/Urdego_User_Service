package io.urdego.urdego_user_service.infra.apple;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class AppleTokenVerifier {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Claims verifyIdToken(String idToken) throws Exception {
        // URL에서 Apple public key 가져오기
        JsonNode publicKeys = fetchApplePublicKeys();

        // ID Token 헤더에서 Key ID 파싱
        String keyId = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(idToken)
                .getHeader()
                .getKeyId();

        // Key ID와 매칭되는 public key 찾기
        PublicKey publicKey = getPublicKey(publicKeys, keyId);

        // JWT 검증, Claim 추출
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(idToken)
                .getBody();

    }

    // Apple public key JSON으로 읽어오기
    private JsonNode fetchApplePublicKeys() throws Exception {
        URL url = new URL("https://appleid.apple.com/auth/keys");
        return objectMapper.readTree(url);
    }

    private PublicKey getPublicKey(JsonNode publicKeys, String keyId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 키 값 매칭
        JsonNode matchingKey = null;
        for (JsonNode key : publicKeys.get("keys")) {
            if (key.get("kid").asText().equals(keyId)) {
                matchingKey = key;
                break;
            }
        }

        if (matchingKey == null) {
            throw new IllegalArgumentException(keyId + "에 해당하는 공개 키가 없습니다.");
        }

        // n, e값 Base64 디코딩
        byte[] nBytes = Base64.getUrlDecoder().decode(matchingKey.get("n").asText());
        byte[] eBytes = Base64.getUrlDecoder().decode(matchingKey.get("e").asText());

        // RSA 공개 키 생성
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(spec);

//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
//            return keyFactory.generatePublic(publicKeySpec);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
//            throw new ExternalServerException("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.");
//        }
    }

}
