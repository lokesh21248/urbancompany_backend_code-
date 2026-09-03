package com.urbanservices.backend.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

/**
 * AWS SDK v2 configuration.
 * Beans only created when aws.enabled=true (skipped in local dev).
 */
@Configuration
@Slf4j
@Getter
public class S3Config {

    @Value("${aws.region:ap-south-1}")
    private String awsRegion;

    @Value("${aws.s3.bucket-name:urban-services-dev}")
    private String bucketName;

    @Value("${aws.s3.presigned-url-expiry-minutes:15}")
    private int presignedUrlExpiryMinutes;

    @Value("${aws.s3.cloudfront-domain:}")
    private String cloudfrontDomain;

    @Value("${aws.sqs.notification-queue-url:}")
    private String notificationQueueUrl;

    @Value("${aws.sqs.email-queue-url:}")
    private String emailQueueUrl;

    @Bean
    @ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = false)
    public S3Client s3Client() {
        log.info("Initializing AWS S3Client — region: {}, bucket: {}", awsRegion, bucketName);
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = false)
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = false)
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
