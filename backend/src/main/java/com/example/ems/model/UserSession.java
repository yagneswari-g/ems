package com.example.ems.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String tokenHash;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private User user;
    @Column(nullable = false) private LocalDateTime expiresAt;

    protected UserSession() {}
    public UserSession(String tokenHash, User user, LocalDateTime expiresAt) {
        this.tokenHash = tokenHash; this.user = user; this.expiresAt = expiresAt;
    }
    public User getUser() { return user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
