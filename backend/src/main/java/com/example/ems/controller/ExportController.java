package com.example.ems.controller;

import com.example.ems.service.ExportService;
import org.springframework.http.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    private final ExportService exports;
    public ExportController(ExportService exports) { this.exports = exports; }
    @GetMapping("/employees.csv") public ResponseEntity<ByteArrayResource> csv(@RequestHeader(value = "X-Auth-Token", required = false) String token) { return download(exports.csv(token), "employees.csv", "text/csv"); }
    @GetMapping("/employees.pdf") public ResponseEntity<ByteArrayResource> pdf(@RequestHeader(value = "X-Auth-Token", required = false) String token) { return download(exports.pdf(token), "employees.pdf", "application/pdf"); }
    private ResponseEntity<ByteArrayResource> download(byte[] bytes, String name, String type) { return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name).body(new ByteArrayResource(bytes)); }
}
