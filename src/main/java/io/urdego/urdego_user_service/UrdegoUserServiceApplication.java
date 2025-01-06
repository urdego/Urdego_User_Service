package io.urdego.urdego_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class UrdegoUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrdegoUserServiceApplication.class, args);
	}

}
