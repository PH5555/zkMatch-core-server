package com.zkrypto.zkMatch.file;

import com.zkrypto.zkMatch.global.file.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@SpringBootTest
public class S3Test {
    @Autowired
    private S3Service s3Service;

//    @Test
//    public void testUploadFile() throws IOException {
//        // 로컬 테스트 리소스 파일 경로
//        String filePath = "C:\\develop\\pika.webp";
//
//        // FileInputStream을 통해 읽기
//        FileInputStream input = new FileInputStream(filePath);
//
//        // MockMultipartFile로 변환
//        MockMultipartFile multipartFile = new MockMultipartFile("file", input);
//
//        // 파일 업로드
//        String uploadedUrl = s3Service.uploadFile(multipartFile);
//
//        log.info("업로드된 URL: " + uploadedUrl);
//    }
}
