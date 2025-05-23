package com.api.fleche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FlecheApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlecheApplication.class, args);
	}

}
