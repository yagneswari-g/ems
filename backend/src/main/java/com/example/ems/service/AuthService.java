package com.example.ems.service;

import com.example.ems.dto.AuthDtos.*;
import com.example.ems.exception.ApiExceptions;
import com.example.ems.model.*;
import com.example.ems.repository.UserRepository;
import com.example.ems.repository.UserSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository users;
    private final UserSessionRepository sessions;
    private final PasswordService passwords;
    private final NotificationService notifications;

    public AuthService(UserRepository users, UserSessionRepository sessions, PasswordService passwords, NotificationService notifications) {
        this.users = users; this.sessions = sessions; this.passwords = passwords; this.notifications = notifications;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new ApiExceptions.BadRequest("Email is already registered");
        if (request.password().length() < 8) throw new ApiExceptions.BadRequest("Password must contain at least 8 characters");
        User user = new User(); user.setEmail(email); user.setFullName(request.fullName().trim());
        user.setPasswordHash(passwords.hash(request.password())); user.setRole(UserRole.EMPLOYEE); users.save(user);
        notifications.send(email, "Welcome to EmployeeHub", "Hello " + user.getFullName() + ", your account was created successfully.");
        return loginUser(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ApiExceptions.Unauthorized("Invalid email or password"));
        if (!passwords.matches(request.password(), user.getPasswordHash())) throw new ApiExceptions.Unauthorized("Invalid email or password");
        return loginUser(user);
    }

    @Transactional
    public void logout(String token) {
        if (token != null && !token.isBlank()) sessions.findByTokenHashAndExpiresAtAfter(passwords.hashToken(token), LocalDateTime.now()).ifPresent(sessions::delete);
    }

    @Transactional(readOnly = true)
    public User requireUser(String token) {
        if (token == null || token.isBlank()) throw new ApiExceptions.Unauthorized("Please log in first");
        return sessions.findByTokenHashAndExpiresAtAfter(passwords.hashToken(token), LocalDateTime.now())
                .orElseThrow(() -> new ApiExceptions.Unauthorized("Session expired or invalid")).getUser();
    }

    public User requireManager(String token) {
        User user = requireUser(token);
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.HR) throw new ApiExceptions.Forbidden("HR or ADMIN access is required");
        return user;
    }

    private AuthResponse loginUser(User user) {
        String plainToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        sessions.save(new UserSession(passwords.hashToken(plainToken), user, LocalDateTime.now().plusHours(12)));
        return new AuthResponse(plainToken, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
