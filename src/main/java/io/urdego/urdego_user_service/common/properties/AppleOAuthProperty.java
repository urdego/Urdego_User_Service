package io.urdego.urdego_user_service.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.apple")
public class AppleOAuthProperty {
    private String redirectUri;
    private String iss;
    private String aud;
    private String teamId;
    private String keyId;
    private String keyPath;
}
