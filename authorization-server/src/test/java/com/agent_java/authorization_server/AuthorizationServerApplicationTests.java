package com.agent_java.authorization_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AuthorizationServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
