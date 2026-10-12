package com.example.ems.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 120) private String email;
    @Column(nullable = false) private String passwordHash;
    @Column(nullable = false, length = 80) private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private UserRole role = UserRole.EMPLOYEE;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String value) { this.passwordHash = value; }
    public String getFullName() { return fullName; }
    public void setFullName(String value) { this.fullName = value; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole value) { this.role = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
