package com.example.ems.service;

import com.example.ems.exception.ApiExceptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root;
    private final long maxBytes;
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir, @Value("${app.max-file-size-mb}") long maxMb) {
        root = Paths.get(uploadDir).toAbsolutePath().normalize(); maxBytes = maxMb * 1024 * 1024;
        try { Files.createDirectories(root.resolve("documents")); Files.createDirectories(root.resolve("profiles")); }
        catch (IOException ex) { throw new IllegalStateException("Cannot create upload folders", ex); }
    }

    public StoredFile store(MultipartFile file, String folder, boolean imageOnly) {
        if (file == null || file.isEmpty()) throw new ApiExceptions.BadRequest("Please select a file");
        if (file.getSize() > maxBytes) throw new ApiExceptions.BadRequest("File must be smaller than " + (maxBytes / 1024 / 1024) + " MB");
        if (imageOnly && !IMAGE_TYPES.contains(file.getContentType())) throw new ApiExceptions.BadRequest("Only JPG, PNG, and WEBP images are allowed");
        String original = file.getOriginalFilename() == null ? "file" : Paths.get(file.getOriginalFilename()).getFileName().toString();
        String stored = UUID.randomUUID() + "-" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = root.resolve(folder).resolve(stored).normalize();
        if (!target.startsWith(root.resolve(folder).normalize())) throw new ApiExceptions.BadRequest("Invalid file name");
        try { Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING); }
        catch (IOException ex) { throw new ApiExceptions.BadRequest("Unable to save file"); }
        return new StoredFile(original, stored, file.getContentType(), file.getSize(), target);
    }

    public byte[] read(String folder, String storedName) {
        try { return Files.readAllBytes(root.resolve(folder).resolve(storedName).normalize()); }
        catch (IOException ex) { throw new ApiExceptions.NotFound("File not found"); }
    }
    public record StoredFile(String originalName, String storedName, String contentType, long size, Path path) {}
}
