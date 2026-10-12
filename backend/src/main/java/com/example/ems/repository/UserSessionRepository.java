package com.example.ems.repository;

import com.example.ems.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByTokenHashAndExpiresAtAfter(String tokenHash, LocalDateTime now);
}
