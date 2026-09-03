package com.urbanservices.backend.service;

public interface FileStorageService {

    /**
     * Directly uploads a byte array to cloud storage.
     *
     * @param folder directory/prefix (e.g. "categories", "avatars")
     * @param filename name of the file (including extension)
     * @param data binary content
     * @param contentType MIME type (e.g. "image/jpeg", "image/png")
     * @return public or CDN URL of the uploaded file
     */
    String uploadFile(String folder, String filename, byte[] data, String contentType);

    /**
     * Generates a pre-signed PUT URL for client-side direct uploads (Flutter / Web).
     *
     * @param folder target folder
     * @param filename target file name
     * @param contentType expected MIME type
     * @return pre-signed upload URL
     */
    String generatePresignedUploadUrl(String folder, String filename, String contentType);

    /**
     * Deletes a file from storage given its key or URL.
     *
     * @param fileUrlOrKey S3 key or full CDN URL
     */
    void deleteFile(String fileUrlOrKey);

    /**
     * Resolves the CDN or public accessible URL for a given storage key.
     *
     * @param s3Key object key in storage
     * @return full accessible URL
     */
    String getCdnUrl(String s3Key);
}
