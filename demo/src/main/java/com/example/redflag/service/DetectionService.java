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


        List<String> urgencyKeywords = List.of(
            "urgent", "immediately", "right now", "as soon as possible",
            "within 24 hours", "act now", "don't delay", "expires soon",
            "last chance", "final notice", "account suspended", "terminated"
        );
        if (urgencyKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 20;
            flags.add("Urgency tactic detected");
        }

    
        List<String> financialKeywords = List.of(
            "bank", "payment", "transaction", "transfer", "wire",
            "processing fee", "prize", "lottery", "won", "winning",
            "claim your", "reward", "refund", "cash", "wallet", "crypto"
        );
        if (financialKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 20;
            flags.add("Financial keyword detected");
        }


        List<String> verifyKeywords = List.of(
            "verify", "verification", "confirm your", "update your",
            "send your", "provide your", "bank details", "account details",
            "personal details", "identity", "validate"
        );
        if (verifyKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 15;
            flags.add("Verification request detected");
        }


        List<String> credentialKeywords = List.of(
            "password", "username", "login", "sign in", "credentials",
            "pin", "secret", "security code"
        );
        if (credentialKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 25;
            flags.add("Sensitive credential request detected");
        }


        List<String> otpKeywords = List.of(
            "otp", "one time password", "one-time", "verification code",
            "auth code", "authentication code"
        );
        if (otpKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 25;
            flags.add("OTP request detected");
        }


        List<String> actionKeywords = List.of(
            "click", "click here", "tap here", "open this link",
            "download", "install", "call now", "contact us immediately"
        );
        if (actionKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 15;
            flags.add("Suspicious action request detected");
        }

        
        List<String> linkKeywords = List.of(
            "http", "www", ".xyz", ".net", ".info", ".click",
            "bit.ly", "tinyurl", "shorturl"
        );
        if (linkKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 25;
            flags.add("Suspicious link detected");
        }

        List<String> kycKeywords = List.of(
            "kyc", "know your customer", "complete your kyc", "kyc update"
        );
        if (kycKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 20;
            flags.add("KYC scam pattern detected");
        }


        List<String> fearKeywords = List.of(
            "expired", "expiring", "blocked", "suspended", "disabled",
            "compromised", "unauthorized", "illegal activity", "legal action",
            "arrested", "penalty", "violation"
        );
        if (fearKeywords.stream().anyMatch(lowerMsg::contains)) {
            riskScore += 15;
            flags.add("Fear/urgency wording detected");
        }

        if (riskScore > 100) riskScore = 100;

        
        String verdict;
        if (riskScore >= 70)      verdict = "Highly Suspicious";
        else if (riskScore >= 35) verdict = "Suspicious";
        else                      verdict = "Safe";

        String aiExplanation = geminiService.analyzeMessage(message);

        response.put("riskScore", riskScore);
        response.put("verdict", verdict);
        response.put("flags", flags);
        response.put("aiExplanation", aiExplanation);

        return response;
    }
}