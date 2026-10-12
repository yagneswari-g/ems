package com.example.ems.controller;

import com.example.ems.dto.ChatDtos.*;
import com.example.ems.service.ChatbotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {
    private final ChatbotService chatbot;
    public ChatbotController(ChatbotService chatbot) { this.chatbot = chatbot; }
    @PostMapping public ChatResponse ask(@RequestHeader(value = "X-Auth-Token", required = false) String token, @Valid @RequestBody ChatRequest request) { return chatbot.ask(token, request.message()); }
}
