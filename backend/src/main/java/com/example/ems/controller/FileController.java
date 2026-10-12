package com.example.ems.controller;

import com.example.ems.exception.ApiExceptions;
import com.example.ems.model.Employee;
import com.example.ems.model.EmployeeDocument;
import com.example.ems.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final AuthService auth; private final EmployeeService employees; private final FileStorageService storage; private final DocumentService documents;
    public FileController(AuthService auth, EmployeeService employees, FileStorageService storage, DocumentService documents) { this.auth = auth; this.employees = employees; this.storage = storage; this.documents = documents; }

    @PostMapping(value = "/profile/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfile(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long employeeId, @RequestPart("file") MultipartFile file) {
        auth.requireManager(token); var saved = storage.store(file, "profiles", true); employees.updateProfileImage(token, employeeId, saved.storedName());
    }

    @GetMapping("/profile/{employeeId}")
    public ResponseEntity<ByteArrayResource> profile(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long employeeId) {
        auth.requireUser(token); Employee e = employees.get(employeeId); if (e.getProfileImagePath() == null) throw new ApiExceptions.NotFound("Profile image not found");
        MediaType type = e.getProfileImagePath().toLowerCase().endsWith(".png") ? MediaType.IMAGE_PNG : e.getProfileImagePath().toLowerCase().endsWith(".webp") ? MediaType.parseMediaType("image/webp") : MediaType.IMAGE_JPEG;
        return fileResponse(storage.read("profiles", e.getProfileImagePath()), type, "profile-image");
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<ByteArrayResource> document(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long documentId) {
        EmployeeDocument d = documents.get(token, documentId); MediaType type = MediaType.APPLICATION_OCTET_STREAM;
        try { if (d.getContentType() != null) type = MediaType.parseMediaType(d.getContentType()); } catch (Exception ignored) {}
        return fileResponse(storage.read("documents", d.getStoredFileName()), type, d.getOriginalFileName());
    }

    private ResponseEntity<ByteArrayResource> fileResponse(byte[] bytes, MediaType type, String name) {
        return ResponseEntity.ok().contentType(type).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name.replace("\"", "") + "\"").body(new ByteArrayResource(bytes));
    }
}
