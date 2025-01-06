package io.urdego.urdego_user_service.common.config;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "io.urdego.urdego_user_service")
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Level.ALL;
    }
}
