package com.project.unknown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnknownApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnknownApplication.class, args);
	}

}
