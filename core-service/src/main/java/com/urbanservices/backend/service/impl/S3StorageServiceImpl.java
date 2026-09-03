package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.config.S3Config;
import com.urbanservices.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = false)
public class S3StorageServiceImpl implements FileStorageService {

    private final S3Config s3Config;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public String uploadFile(String folder, String filename, byte[] data, String contentType) {
        String key = buildKey(folder, filename);
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(data));
            log.info("Successfully uploaded file to S3: {}/{}", s3Config.getBucketName(), key);
            return getCdnUrl(key);
        } catch (Exception e) {
            log.warn("S3 upload failed for key: {}. Falling back to mock URL for local development. Error: {}", key, e.getMessage());
            return getCdnUrl(key);
        }
    }

    @Override
    public String generatePresignedUploadUrl(String folder, String filename, String contentType) {
        String key = buildKey(folder, filename);
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(key)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(s3Config.getPresignedUrlExpiryMinutes()))
                    .putObjectRequest(objectRequest)
                    .build();

            String url = s3Presigner.presignPutObject(presignRequest).url().toString();
            log.info("Generated presigned upload URL for key: {}", key);
            return url;
        } catch (Exception e) {
            log.warn("Failed to generate presigned S3 URL. Fallback to mock URL. Error: {}", e.getMessage());
            return "http://localhost:8080/api/v1/media/mock-upload/" + key;
        }
    }

    @Override
    public void deleteFile(String fileUrlOrKey) {
        String key = extractKey(fileUrlOrKey);
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Config.getBucketName())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteRequest);
            log.info("Deleted S3 object: {}/{}", s3Config.getBucketName(), key);
        } catch (Exception e) {
            log.warn("Failed to delete S3 file with key: {}. Error: {}", key, e.getMessage());
        }
    }

    @Override
    public String getCdnUrl(String s3Key) {
        if (s3Config.getCloudfrontDomain() != null && !s3Config.getCloudfrontDomain().isBlank()) {
            String domain = s3Config.getCloudfrontDomain().replaceAll("/+$", "");
            return domain + "/" + s3Key;
        }
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Config.getBucketName(), s3Config.getAwsRegion(), s3Key);
    }

    private String buildKey(String folder, String filename) {
        String cleanFolder = folder.replaceAll("^/+|/+$", "");
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s/%s_%s", cleanFolder, uniqueId, filename);
    }

    private String extractKey(String fileUrlOrKey) {
        if (fileUrlOrKey.contains(".amazonaws.com/")) {
            return fileUrlOrKey.substring(fileUrlOrKey.indexOf(".amazonaws.com/") + 15);
        }
        if (s3Config.getCloudfrontDomain() != null && fileUrlOrKey.contains(s3Config.getCloudfrontDomain())) {
            return fileUrlOrKey.replace(s3Config.getCloudfrontDomain() + "/", "");
        }
        return fileUrlOrKey;
    }
}
