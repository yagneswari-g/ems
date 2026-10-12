package com.example.ems.dto;

import jakarta.validation.constraints.NotBlank;

public final class ChatDtos {
    private ChatDtos() {}
    public record ChatRequest(@NotBlank String message) {}
    public record ChatResponse(String reply) {}
}
