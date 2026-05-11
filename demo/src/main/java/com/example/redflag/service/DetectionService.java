package com.example.redflag.service;

import com.example.redflag.engine.RuleEngine;
import com.example.redflag.engine.AIEngine;
import com.example.redflag.engine.ScoringEngine;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DetectionService {

    private final RuleEngine ruleEngine = new RuleEngine();
    private final AIEngine aiEngine = new AIEngine();
    private final ScoringEngine scoringEngine = new ScoringEngine();

    public Map<String, Object> analyze(String message) {

        Map<String, Object> ruleResult = ruleEngine.analyze(message);
        Map<String, Object> aiResult = aiEngine.analyze(message);

        int finalScore = scoringEngine.calculateScore(ruleResult, aiResult);

        String verdict = finalScore > 60 ? "High Risk" :
                finalScore > 30 ? "Suspicious" : "Safe";

        Map<String, Object> response = new HashMap<>();
        response.put("riskScore", finalScore);
        response.put("verdict", verdict);
        response.put("flags", ruleResult.get("flags"));
        response.put("aiExplanation", aiResult.get("explanation"));

        return response;
    }
}