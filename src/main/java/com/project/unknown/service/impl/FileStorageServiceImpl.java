package com.project.unknown.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir + "profiles"));
            Files.createDirectories(Paths.get(uploadDir + "posts"));
            log.info("Upload directories created successfully");
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    //Speichere Profile Bild

    public String saveProfileImage(MultipartFile file, Long userId) {
        validateImageFile(file);
        deleteOldProfileImage(userId);

        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = "user_" + userId + extension;

        Path targetLocation = Paths.get(uploadDir + "profiles/" + fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Profile image saved: {}", targetLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName, e);
        }

        return "/uploads/profiles/" + fileName;
    }

     //Speichere Post Medien (Bilder/Videos)
    public String savePostMedia(MultipartFile file, Long postId) {
        validateMediaFile(file);

        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = "post_" + postId + "_" + UUID.randomUUID() + extension;

        Path targetLocation = Paths.get(uploadDir + "posts/" + fileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Post media saved: {}", targetLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName, e);
        }

        return "/uploads/posts/" + fileName;
    }
    //Datei löschen
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir + filePath.replace("/uploads/", ""));
            Files.deleteIfExists(path);
            log.info("File deleted: {}", path);
        } catch (IOException e) {
            log.error("Could not delete file: {}", filePath, e);
        }
    }

    //Altes Profile Bild löschen
    private void deleteOldProfileImage(Long userId) {
        try {
            String pattern = "user_" + userId + ".*";
            Path dir = Paths.get(uploadDir + "profiles/");

            if (!Files.exists(dir)) {
                return;
            }

            Files.list(dir)
                    .filter(path -> path.getFileName().toString().matches(pattern))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            log.info("Deleted old profile image: {}", path);
                        } catch (IOException e) {
                            log.error("Could not delete old profile image", e);
                        }
                    });
        } catch (IOException e) {
            log.error("Could not delete old profile image", e);
        }
    }

    //Validierung für Bilder
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only images are allowed");
        }

        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif");
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, and GIF images are allowed");
        }
    }

    // Validierung für Post Media (Bilder + Videos)

    private void validateMediaFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Invalid file type");
        }

        if (contentType.startsWith("image/")) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Image size must be less than 10MB");
            }

            List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif");
            if (!allowedTypes.contains(contentType)) {
                throw new IllegalArgumentException("Only JPEG, PNG, and GIF images are allowed");
            }
        } else if (contentType.startsWith("video/")) {
            if (file.getSize() > 50 * 1024 * 1024) {
                throw new IllegalArgumentException("Video size must be less than 50MB");
            }

            List<String> allowedTypes = Arrays.asList("video/mp4", "video/mpeg", "video/quicktime");
            if (!allowedTypes.contains(contentType)) {
                throw new IllegalArgumentException("Only MP4, MPEG, and MOV videos are allowed");
            }
        } else {
            throw new IllegalArgumentException("Only images and videos are allowed");
        }
    }

    //Dateiendung extrahieren

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}
