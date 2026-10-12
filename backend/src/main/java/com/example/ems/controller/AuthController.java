package com.example.ems.controller;

import com.example.ems.dto.AuthDtos.*;
import com.example.ems.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }
    @PostMapping("/register") public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) { return ResponseEntity.ok(auth.register(request)); }
    @PostMapping("/login") public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) { return ResponseEntity.ok(auth.login(request)); }
    @PostMapping("/logout") public ResponseEntity<Void> logout(@RequestHeader(value = "X-Auth-Token", required = false) String token) { auth.logout(token); return ResponseEntity.noContent().build(); }
    @GetMapping("/me") public ResponseEntity<AuthResponse> me(@RequestHeader(value = "X-Auth-Token", required = false) String token) { var user = auth.requireUser(token); return ResponseEntity.ok(new AuthResponse(null, user.getId(), user.getFullName(), user.getEmail(), user.getRole())); }
}
