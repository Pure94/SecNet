package com.larpologic.secretnetwork.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenRouterClient {

    private final WebClient webClient;

    public OpenRouterClient(@Value("${openrouter.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader("X-Title", "NeuroBioMyko")
                .build();
    }

    public String getCompletion(OpenRouterRequest request) {
        OpenRouterResponse response = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenRouterResponse.class)
                .block();

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        return "Brak odpowiedzi od AI.";
    }
}