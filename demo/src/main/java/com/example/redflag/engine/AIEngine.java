package com.example.redflag.engine;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class AIEngine {

    private static final String API_KEY = "YOUR_API_KEY";

    public Map<String, Object> analyze(String message) {

        String responseText = "AI analysis unavailable";

        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = "Analyze this message for scam. Explain briefly: " + message;

            String jsonInput = "{"
                    + "\"model\": \"gpt-4o\","
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]"
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            responseText = result.toString();

        } catch (Exception e) {
            responseText = "AI error";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("explanation", responseText);
        map.put("score", 30); // fixed AI weight

        return map;
    }
}