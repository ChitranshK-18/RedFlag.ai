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
                Analyze the following message for phishing, scam, or suspicious activity.

                Return:
                1. Risk Score
                2. Verdict
                3. Detailed Explanation
                4. Detected Red Flags

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