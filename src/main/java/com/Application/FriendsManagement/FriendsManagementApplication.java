package com.Application.FriendsManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class FriendsManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsManagementApplication.class, args);
	}

}
