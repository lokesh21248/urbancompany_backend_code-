package com.urbanservices.backend.controller;

import com.urbanservices.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final Path rootPath = Paths.get("uploads").toAbsolutePath().normalize();

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {

        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "File cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate clean unique filename: timestamp_uuid.ext
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String cleanName = System.currentTimeMillis() + "_" + uniqueId + extension.toLowerCase();

        try {
            String fileUrl = fileStorageService.uploadFile(folder, cleanName, file.getBytes(), file.getContentType());

            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("fileName", cleanName);
            response.put("originalName", originalFilename);
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());

            log.info("File uploaded successfully: {} -> {}", originalFilename, fileUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("File upload failed for {}", originalFilename, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/files/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String folder,
            @PathVariable String filename) {
        try {
            Path safeFolder = rootPath.resolve(folder.replaceAll("[^a-zA-Z0-9_-]", "")).normalize();
            Path filePath = safeFolder.resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                String lower = filename.toLowerCase();
                if (lower.endsWith(".png")) contentType = "image/png";
                else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) contentType = "image/jpeg";
                else if (lower.endsWith(".webp")) contentType = "image/webp";
                else if (lower.endsWith(".svg")) contentType = "image/svg+xml";
                else if (lower.endsWith(".gif")) contentType = "image/gif";
                else contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(resource);
        } catch (MalformedURLException e) {
            log.error("Malformed URL for file: {}/{}", folder, filename, e);
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            log.error("Error reading file: {}/{}", folder, filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFileWithoutFolder(@PathVariable String filename) {
        return getFile("general", filename);
    }
}
