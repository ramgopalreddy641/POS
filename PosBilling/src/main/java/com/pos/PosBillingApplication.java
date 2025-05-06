package com.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PosBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosBillingApplication.class, args);
	}

}