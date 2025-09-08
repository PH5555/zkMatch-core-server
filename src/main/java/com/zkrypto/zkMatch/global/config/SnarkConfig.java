package com.zkrypto.zkMatch.global.config;

import com.zkrypto.snark.SNARK;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Configuration
@Slf4j
public class SnarkConfig {
    @Value("classpath:libOpenDID_Hackathon.dylib")
    private Resource snarkLibraryForMac;

    @Value("classpath:libOpenDID_Hackathon.so")
    private Resource snarkLibraryForLinux;

    @PostConstruct
    private void loadSnarkLibrary() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {
            System.out.println("Loading Linux (.so) library...");
            loadLibraryFromResource(snarkLibraryForLinux);
        }
        else if (os.contains("mac")) {
            System.out.println("Loading macOS (.dylib) library...");
            loadLibraryFromResource(snarkLibraryForMac);
        }
        else {
            System.out.println("Not Support os for Snark");
        }
    }

    private void loadLibraryFromResource(Resource resource) {
        // 1. 리소스로부터 InputStream을 얻습니다.
        try (InputStream inputStream = resource.getInputStream()) {

            // 2. 임시 파일을 생성합니다. (예: /tmp/libOpenDID_Hackathon-12345.so)
            String[] parts = resource.getFilename().split("\\.");
            String prefix = parts[0];
            String suffix = "." + parts[1];
            File tempFile = File.createTempFile(prefix, suffix);

            // 3. JVM 종료 시 임시 파일을 자동으로 삭제하도록 설정합니다. (중요!)
            tempFile.deleteOnExit();

            // 4. 리소스의 내용을 임시 파일에 복사합니다.
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 5. 임시 파일의 절대 경로를 사용하여 네이티브 라이브러리를 로드합니다.
            System.load(tempFile.getAbsolutePath());

            System.out.println("Successfully loaded library from: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
