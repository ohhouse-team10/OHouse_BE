package com.sparta.todayhouse.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.todayhouse.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.sparta.todayhouse.error.ErrorCode.FAIL_TO_UPLOAD;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public ResponseMessage<?> uploadFile(MultipartFile multipartFile){
        //파일 이름 가져오기 및 생성
        String fileName = multipartFile.getOriginalFilename();
        //String extension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + "-" + fileName;

        //파일 MetaData 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        //S3에 업로드
        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, newFileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            return ResponseMessage.fail(FAIL_TO_UPLOAD);
        }

        //파일 url 반환
        return ResponseMessage.success(amazonS3Client.getUrl(bucket, newFileName).toString());
    }
}
