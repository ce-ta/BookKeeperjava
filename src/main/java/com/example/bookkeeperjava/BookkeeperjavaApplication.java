package com.example.bookkeeperjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookkeeperjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookkeeperjavaApplication.class, args);
	}

}
