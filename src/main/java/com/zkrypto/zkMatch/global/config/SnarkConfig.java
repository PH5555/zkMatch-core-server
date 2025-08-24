package com.zkrypto.zkMatch.global.config;

import com.zkrypto.snark.SNARK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SnarkConfig {
    public SnarkConfig() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {
            System.load(System.getProperty("user.dir") + "/libs/libOpenDID_Hackathon.so");
            String[] keys = SNARK.generateCircuit();
            log.info("ek:" + keys[0]);
            log.info("vk:" + keys[1]);
        } else if (os.contains("mac")) {
            System.load(System.getProperty("user.dir") + "/libs/libOpenDID_Hackathon.dylib");
            String[] keys = SNARK.generateCircuit();
            log.info("ek:" + keys[0]);
            log.info("vk:" + keys[1]);
        }
    }
}
