package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringOrientDBApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOrientDBApplication.class, args);
	}
}
