package com.example.redflag.engine;

import java.util.Map;

public class ScoringEngine {

    public int calculateScore(Map<String, Object> rule, Map<String, Object> ai) {

        int ruleScore = (int) rule.get("score");
        int aiScore = (int) ai.get("score");

        return Math.min(ruleScore + aiScore, 100);
    }
}