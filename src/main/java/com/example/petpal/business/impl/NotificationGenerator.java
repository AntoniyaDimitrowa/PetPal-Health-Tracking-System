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

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    public String generateNotification(String anomalies) {
        if (anomalies == null || anomalies.trim().isEmpty()) {
            return "Your pet is in good health!";
        }

        RestTemplate restTemplate = new RestTemplate();

        // Create the request payload
        Map<String, Object> request = new HashMap<>();
        request.put("model", model);
        request.put("prompt", buildPrompt(anomalies)); // Use "prompt" instead of "messages"
        request.put("temperature", 0.7);
        request.put("max_tokens", 100);

        // Set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        headers.put("Content-Type", "application/json");

        // Call the API
        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);

            // Extract the generated message
            if (response != null && response.containsKey("choices")) {
                var choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    var choice = choices.get(0);
                    if (choice.containsKey("text")) { // Expect "text" in the response, not "message"
                        return choice.get("text").toString().trim();
                    }
                }
            }
        } catch (Exception e) {
            String formattedAnomalies = anomalies.replace(", ", "\n").replace(";", "\n");
            return "There was an issue generating a notification for your pet, but these are the raw results: \n" + formattedAnomalies;
        }

        // Fallback in case no valid response or text is found
        String formattedAnomalies = anomalies.replace(", ", "\n").replace(";", "\n");
        return "There was an issue generating a notification for your pet, but these are the raw results: \n" + formattedAnomalies;
    }

    private String buildPrompt(String anomalies) {
        return "Generate a caring notification for the following pet health issues: " + anomalies + ".";
    }
}
