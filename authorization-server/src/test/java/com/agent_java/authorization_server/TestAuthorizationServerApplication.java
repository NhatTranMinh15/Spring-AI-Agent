package com.agent_java.authorization_server;

import org.springframework.boot.SpringApplication;

public class TestAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuthorizationServerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
