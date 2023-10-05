package com.github.valentina810.weekplannerformarusia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WeekPlannerForMarusiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeekPlannerForMarusiaApplication.class, args);
	}
}