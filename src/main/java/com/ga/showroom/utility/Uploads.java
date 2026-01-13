package com.ga.showroom.utility;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Component utility class to handle files
 */
@Component
public class Uploads {

    /**
     * Upload an image file.
     * @param uploadPath Path resolved filepath including storage location + image name.
     * @param image MultipartFile image [PNG, JPEG, JPG]
     * @return String new uploaded file's name
     * @throws BadRequestException Bad upload request handling
     */
    public String uploadImage(String uploadPath, MultipartFile image) throws BadRequestException {

        if (image.isEmpty()) throw new BadRequestException("Image is empty");

        String fileType = image.getContentType();

        if (!Objects.equals(fileType, "image/jpeg")
                && !Objects.equals(fileType, "image/jpg")
                && !Objects.equals(fileType, "image/png")) {
            throw new BadRequestException("Invalid image file type. Only .PNG, .JPEG, AND .JPG allowed");
        }

        try {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Path.of(uploadPath);

            java.nio.file.Files.createDirectories(path);

            java.nio.file.Files.copy(
                    image.getInputStream(),
                    path.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    /**
     * Delete an existing image from storage.
     * @param filePath Path resolved filepath including storage location + image name.
     */
    public void deleteImage(Path filePath) {
        try {
            java.nio.file.Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
