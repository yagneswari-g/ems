package com.example.ems.controller;

import com.example.ems.dto.EmployeeDtos.DocumentResponse;
import com.example.ems.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/employees/{employeeId}/documents")
public class DocumentController {
    private final DocumentService documents;
    public DocumentController(DocumentService documents) { this.documents = documents; }
    @GetMapping public List<DocumentResponse> list(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long employeeId) { return documents.list(token, employeeId); }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) public DocumentResponse upload(@RequestHeader(value = "X-Auth-Token", required = false) String token, @PathVariable Long employeeId, @RequestPart("file") MultipartFile file) { return documents.upload(token, employeeId, file); }
}
