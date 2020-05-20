package com.ashu.practice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ashu.practice.controller.HelloController;

@SpringBootTest
class AuthenticationApplicationTests {

	@Autowired
	private HelloController helloController;

	@Test
	void contextLoads() {
		assertThat(helloController).isNotNull();
	}

}
