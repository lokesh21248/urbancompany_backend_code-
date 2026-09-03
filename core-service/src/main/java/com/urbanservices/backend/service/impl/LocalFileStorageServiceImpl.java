package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Local file storage service that stores files to disk under ./uploads.
 * Used in local dev when AWS S3 is not configured.
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "aws.enabled", havingValue = "false", matchIfMissing = true)
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final Path rootPath = Paths.get("uploads").toAbsolutePath().normalize();

    public LocalFileStorageServiceImpl() {
        try {
            Files.createDirectories(rootPath);
            log.info("[LOCAL STORAGE] Initialized upload directory at: {}", rootPath);
        } catch (IOException e) {
            log.error("[LOCAL STORAGE] Could not create uploads directory: {}", rootPath, e);
        }
    }

    @Override
    public String uploadFile(String folder, String filename, byte[] data, String contentType) {
        try {
            String safeFolder = (folder == null || folder.isBlank()) ? "general" : folder.replaceAll("[^a-zA-Z0-9_-]", "");
            Path targetDir = rootPath.resolve(safeFolder).normalize();
            Files.createDirectories(targetDir);

            Path targetFile = targetDir.resolve(filename).normalize();
            Files.write(targetFile, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("[LOCAL STORAGE] Saved file: {}", targetFile);

            return "/api/v1/files/" + safeFolder + "/" + filename;
        } catch (IOException e) {
            log.error("[LOCAL STORAGE] Failed to write file: {}/{}", folder, filename, e);
            throw new RuntimeException("Failed to store file locally: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String url) {
        if (url == null || url.isBlank()) return;
        try {
            if (url.contains("/api/v1/files/")) {
                String sub = url.substring(url.indexOf("/api/v1/files/") + "/api/v1/files/".length());
                Path target = rootPath.resolve(sub).normalize();
                if (Files.exists(target)) {
                    Files.delete(target);
                    log.info("[LOCAL STORAGE] Deleted file: {}", target);
                }
            }
        } catch (IOException e) {
            log.warn("[LOCAL STORAGE] Could not delete file {}: {}", url, e.getMessage());
        }
    }

    @Override
    public String generatePresignedUploadUrl(String folder, String filename, String contentType) {
        return "/api/v1/upload?folder=" + (folder != null ? folder : "general");
    }

    @Override
    public String getCdnUrl(String s3Key) {
        if (s3Key == null) return null;
        if (s3Key.startsWith("http://") || s3Key.startsWith("https://") || s3Key.startsWith("/")) {
            return s3Key;
        }
        return "/api/v1/files/" + s3Key;
    }
}
