package io.urdego.urdego_user_service.common.config;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Level.ALL;
    }
}
