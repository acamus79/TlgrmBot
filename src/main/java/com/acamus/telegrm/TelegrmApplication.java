package com.acamus.telegrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegrmApplication.class, args);
	}

}
