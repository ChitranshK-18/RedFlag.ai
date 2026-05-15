package com.example.redflag.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final String API_KEY = "AIzaSyCj71yqhCjZMAnr51aRi3TNAsr4VLnEJWg";

    private final WebClient webClient = WebClient.create();

    public String analyzeMessage(String message) {

        String prompt = """
        Analyze this message for scam/phishing risk.

        Give:
        1. Threat Summary
        2. Why it is suspicious
        3. Risk level
        4. User safety advice

        Keep the response concise and professional.

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
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                        + API_KEY;

        try {

            Map response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)  // ✅ This was missing
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {

                Map<String, Object> content =
                        (Map<String, Object>) candidates.get(0).get("content");

                List<Map<String, Object>> parts =
                        (List<Map<String, Object>>) content.get("parts");

                return parts.get(0).get("text").toString();
            }

            return "AI could not analyze the message.";

        } catch (Exception e) {

            e.printStackTrace();
            return "AI could not analyze the message. Error: " + e.getMessage();  // ✅ Show actual error
        }
    }
}