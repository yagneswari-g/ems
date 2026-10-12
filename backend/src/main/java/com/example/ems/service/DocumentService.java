package com.example.ems.service;

import com.example.ems.dto.EmployeeDtos.DocumentResponse;
import com.example.ems.exception.ApiExceptions;
import com.example.ems.model.EmployeeDocument;
import com.example.ems.repository.EmployeeDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class DocumentService {
    private final EmployeeDocumentRepository documents;
    private final EmployeeService employees;
    private final FileStorageService storage;
    private final AuthService auth;
    private final NotificationService notifications;

    public DocumentService(EmployeeDocumentRepository documents, EmployeeService employees, FileStorageService storage, AuthService auth, NotificationService notifications) {
        this.documents = documents; this.employees = employees; this.storage = storage; this.auth = auth; this.notifications = notifications;
    }
    public DocumentResponse upload(String token, Long employeeId, MultipartFile file) {
        auth.requireManager(token); var employee = employees.get(employeeId); var saved = storage.store(file, "documents", false);
        EmployeeDocument d = new EmployeeDocument(); d.setEmployee(employee); d.setOriginalFileName(saved.originalName()); d.setStoredFileName(saved.storedName()); d.setContentType(saved.contentType()); d.setSize(saved.size()); documents.save(d);
        notifications.send(employee.getEmail(), "Employee document uploaded", "A document was uploaded to your EmployeeHub profile.");
        return toResponse(d);
    }
    public List<DocumentResponse> list(String token, Long employeeId) { auth.requireUser(token); employees.get(employeeId); return documents.findByEmployeeIdOrderByUploadedAtDesc(employeeId).stream().map(this::toResponse).toList(); }
    public EmployeeDocument get(String token, Long documentId) { auth.requireUser(token); return documents.findById(documentId).orElseThrow(() -> new ApiExceptions.NotFound("Document not found")); }
    private DocumentResponse toResponse(EmployeeDocument d) { return new DocumentResponse(d.getId(), d.getOriginalFileName(), d.getContentType(), d.getSize(), d.getUploadedAt(), "/api/files/documents/" + d.getId()); }
}
