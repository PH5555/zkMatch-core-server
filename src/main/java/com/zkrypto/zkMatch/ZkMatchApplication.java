package com.zkrypto.zkMatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class ZkMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZkMatchApplication.class, args);
	}

}
