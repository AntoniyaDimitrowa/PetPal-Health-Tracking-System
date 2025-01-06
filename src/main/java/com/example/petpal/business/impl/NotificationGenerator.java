package com.example.petpal.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationGenerator {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public String generateNotification(String anomalies) {
        // If no anomalies, return a positive message
        if (anomalies == null || anomalies.trim().isEmpty()) {
            return "Your pet is in good health!";
        }

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> request = new HashMap<>();
        request.put("model", "text-davinci-003");
        request.put("prompt", buildPrompt(anomalies));
        request.put("temperature", 0.7);
        request.put("max_tokens", 100);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + openAiApiKey);
        headers.put("Content-Type", "application/json");

        // Call the OpenAI API and receive the response
        Map<String, Object> response = restTemplate.postForObject(openAiApiUrl, request, Map.class, headers);

        // Ensure response contains choices and safely extract the message
        if (response != null && response.containsKey("choices")) {
            Object choicesObject = response.get("choices");
            if (choicesObject instanceof List<?>) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObject;
                if (!choices.isEmpty()) {
                    // Safely extract the text from the first choice
                    Map<String, Object> firstChoice = choices.get(0);
                    if (firstChoice.containsKey("text")) {
                        return firstChoice.get("text").toString().trim();
                    }
                }
            }
        }

        // Fallback in case no valid response or text is found
        String formattedAnomalies = anomalies.replace(", ", "\n").replace(";", "\n");
        return "There was an issue generating a notification for your pet, but these are the raw results: \n" + formattedAnomalies;
    }

    private String buildPrompt(String anomalies) {
        return "Generate a caring notification for the following issues: " + anomalies + ".";
    }
}
