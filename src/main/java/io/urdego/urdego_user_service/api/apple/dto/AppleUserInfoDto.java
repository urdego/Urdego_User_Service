package io.urdego.urdego_user_service.api.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AppleUserInfoDto {

    @JsonProperty("sub")
    private String platformId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private String emailVerified;

    @JsonProperty("is_private_email")
    private String isPrivateEmail;

    private String nickname = splitEmail(email);

    private PlatformType platformType = PlatformType.APPLE;

    private static String splitEmail(String email) {
        String nickname[] = email.split("@");
        return nickname[0];
    }

    public static AppleUserInfoDto of(Claims claims) {
        AppleUserInfoDto dto = new AppleUserInfoDto();
        dto.platformId = claims.getSubject();
        dto.email = claims.get("email", String.class);
        dto.emailVerified = claims.get("email_verified", String.class);
        dto.isPrivateEmail = claims.get("is_private_email", String.class);
        dto.nickname = splitEmail(dto.email);
        dto.platformType = PlatformType.APPLE;
        return dto;
    }

}
