package com.example.redflag.engine;

import java.util.*;

public class RuleEngine {

    public Map<String, Object> analyze(String message) {

        int score = 0;
        List<String> flags = new ArrayList<>();
        String msg = message.toLowerCase();

        String[] keywords = {"urgent", "otp", "blocked", "prize", "lottery", "bank"};

        for (String word : keywords) {
            if (msg.contains(word)) {
                score += 10;
                flags.add("Keyword detected: " + word);
            }
        }

        if (msg.contains("bit.ly") || msg.contains("tinyurl")) {
            score += 20;
            flags.add("Shortened URL detected");
        }

        if (msg.contains("+92") || msg.contains("+44")) {
            score += 20;
            flags.add("International number detected");
        }

        if (msg.contains("tonight") || msg.contains("immediately")) {
            score += 10;
            flags.add("Urgency language detected");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("flags", flags);

        return result;
    }
}