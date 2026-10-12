package com.example.ems.service;

import com.example.ems.dto.ChatDtos.ChatResponse;
import com.example.ems.exception.ApiExceptions;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Service
public class ChatbotService {

    private static final Logger log =
            LoggerFactory.getLogger(ChatbotService.class);

    private final AuthService auth;
    private final RestClient client;
    private final String apiKey;
    private final String model;

    public ChatbotService(
            AuthService auth,
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.model}") String model) {

        this.auth = auth;
        this.client = RestClient.create();
        this.apiKey = apiKey;
        this.model = model;
    }

    public ChatResponse ask(String token, String message) {

        auth.requireUser(token);

        if (apiKey == null || apiKey.isBlank()) {
            return new ChatResponse(
                    "Gemini is not configured. Add GEMINI_API_KEY to the backend environment."
            );
        }

        if (message == null || message.isBlank()) {
            throw new ApiExceptions.BadRequest(
                    "Chat message cannot be empty"
            );
        }

        String prompt =
                "You are a helpful EmployeeHub assistant. "
                        + "Answer briefly and clearly. "
                        + "Never request, reveal, modify, or invent private employee records. "
                        + "User question: "
                        + message;

        Map<String, Object> request = Map.of(
                "contents",
                new Object[]{
                        Map.of(
                                "role",
                                "user",
                                "parts",
                                new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        try {
            JsonNode response = client.post()
                    .uri(
                            "https://generativelanguage.googleapis.com/"
                                    + "v1beta/models/{model}:generateContent",
                            model
                    )
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

            String reply = response == null
                    ? null
                    : response
                    .at("/candidates/0/content/parts/0/text")
                    .asText(null);

            if (reply == null || reply.isBlank()) {
                throw new ApiExceptions.BadRequest(
                        "Gemini returned an empty response"
                );
            }

            return new ChatResponse(reply);

        } catch (ApiExceptions.BadRequest ex) {
            throw ex;

        } catch (RestClientResponseException ex) {
            log.error(
                    "Gemini API error: status={}, response={}",
                    ex.getStatusCode().value(),
                    ex.getResponseBodyAsString()
            );

            throw new ApiExceptions.BadRequest(
                    "Gemini API returned HTTP "
                            + ex.getStatusCode().value()
            );

        } catch (Exception ex) {
            log.error("Unexpected Gemini error", ex);

            throw new ApiExceptions.BadRequest(
                    "Gemini request failed. Check the backend console."
            );
        }
    }
}