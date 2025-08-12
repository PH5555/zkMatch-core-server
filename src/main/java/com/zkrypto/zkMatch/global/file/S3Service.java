package com.zkrypto.zkMatch.global.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * S3에 이미지 업로드 하기
     */
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 고유한 파일 이름 생성

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = null;
        try {
            putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_UPLOAD_FILE);
        }

        // S3에 파일 업로드
        amazonS3.putObject(putObjectRequest);

        return getPublicUrl(fileName);
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }
}