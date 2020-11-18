package com.sxj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.sxj.service",
})
public class Neo4jDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jDemoApplication.class, args);
	}
}
