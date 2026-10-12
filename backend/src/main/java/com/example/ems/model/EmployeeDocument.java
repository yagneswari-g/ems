package com.example.ems.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_documents")
public class EmployeeDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Employee employee;
    @Column(nullable = false) private String originalFileName;
    @Column(nullable = false) private String storedFileName;
    private String contentType;
    private long size;
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee value) { employee = value; }
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String value) { originalFileName = value; }
    public String getStoredFileName() { return storedFileName; }
    public void setStoredFileName(String value) { storedFileName = value; }
    public String getContentType() { return contentType; }
    public void setContentType(String value) { contentType = value; }
    public long getSize() { return size; }
    public void setSize(long value) { size = value; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}
