package com.zkrypto.zkMatch;

import com.zkrypto.zkMatch.mockUp.MockUpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zkrypto.zkMatch.domain", "com.zkrypto.zkMatch.global"})
@Import(MockUpHelper.class)
@EnableJpaAuditing
@EnableFeignClients
@Slf4j
public class ZkMatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZkMatchApplication.class, args);
    }
}
