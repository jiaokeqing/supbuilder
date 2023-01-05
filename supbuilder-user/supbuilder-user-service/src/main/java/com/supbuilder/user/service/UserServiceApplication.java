package com.supbuilder.user.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.supbuilder.user.service.mapper")
public class UserServiceApplication {

	public static void main(String[] args) {

		try {
			SpringApplication.run(UserServiceApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
