package com.example.redflag.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DetectionService {

    private final GeminiService geminiService;

    public DetectionService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public Map<String, Object> analyze(String message) {

        Map<String, Object> response = new HashMap<>();

        String lowerMsg = message.toLowerCase();

        int riskScore = 0;

        List<String> flags = new ArrayList<>();


        // RULES

        if(lowerMsg.contains("urgent")) {
            riskScore += 20;
            flags.add("Urgency tactic detected");
        }

        if(lowerMsg.contains("bank")) {
            riskScore += 20;
            flags.add("Financial keyword detected");
        }

        if(lowerMsg.contains("verify")) {
            riskScore += 15;
            flags.add("Verification request detected");
        }

        if(lowerMsg.contains("password")) {
            riskScore += 25;
            flags.add("Sensitive credential request detected");
        }

        if(lowerMsg.contains("otp")) {
            riskScore += 25;
            flags.add("OTP request detected");
        }

        if(lowerMsg.contains("click")) {
            riskScore += 15;
            flags.add("Suspicious action request detected");
        }

        if(lowerMsg.contains("http") || lowerMsg.contains("www")) {
            riskScore += 25;
            flags.add("Suspicious link detected");
        }

        if(lowerMsg.contains("kyc")) {
            riskScore += 20;
            flags.add("KYC scam pattern detected");
        }

        if(lowerMsg.contains("expired")) {
            riskScore += 10;
            flags.add("Fear/urgency wording detected");
        }


        // LIMIT

        if(riskScore > 100) {
            riskScore = 100;
        }


        // VERDICT

        String verdict;

        if(riskScore >= 70) {
            verdict = "Highly Suspicious";
        }
        else if(riskScore >= 35) {
            verdict = "Suspicious";
        }
        else {
            verdict = "Safe";
        }


        // REAL AI ANALYSIS

        String aiExplanation = geminiService.analyzeMessage(message);


        // RESPONSE

        response.put("riskScore", riskScore);
        response.put("verdict", verdict);
        response.put("flags", flags);
        response.put("aiExplanation", aiExplanation);

        return response;
    }
}