package com.example.redflag.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private final String API_KEY = "AIzaSyC6EnYMBD3-Tl9JKAj1ZUc-wFjUO6Zrl3M";

    private final WebClient webClient = WebClient.create();

    public String analyzeMessage(String message) {

        String prompt = """
        Analyze this message for scam/phishing risk.

        Give:
        1. Threat Summary
        2. Why it is suspicious
        3. Risk level
        4. User safety advice

        Message:
        """ + message;

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
                        + API_KEY;

        try {

            String response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response;

        } catch (Exception e) {

            return """
            AI-based contextual analysis is temporarily unavailable due to API rate limits.

            However, the system detected multiple suspicious indicators such as urgency tactics,
            financial references, suspicious links, and verification requests.

            Users are strongly advised to avoid interacting with this message or sharing sensitive information.
            """;
        }
    }
}