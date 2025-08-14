package com.zkrypto.zkMatch;

import com.zkrypto.zkMatch.mockUp.MockUpHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zkrypto.zkMatch.domain", "com.zkrypto.zkMatch.global"})
@Import(MockUpHelper.class)
@EnableJpaAuditing
@EnableFeignClients
public class ZkMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZkMatchApplication.class, args);
	}

}
