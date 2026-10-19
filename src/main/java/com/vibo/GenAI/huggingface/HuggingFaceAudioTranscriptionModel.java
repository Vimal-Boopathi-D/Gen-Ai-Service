package com.vibo.GenAI.huggingface;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import java.io.IOException;
import java.util.Map;

public class HuggingFaceAudioTranscriptionModel {

    private final RestClient restClient;
    private final String apiUrl;

    public HuggingFaceAudioTranscriptionModel(String apiKey, String apiUrl) {
        this.apiUrl = apiUrl;
        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public String transcribe(byte[] audioBytes) {
        try {
            Map response = restClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "audio/flac")
                    .body(audioBytes)
                    .retrieve()
                    .body(Map.class);
            
            if (response != null && response.containsKey("text")) {
                return (String) response.get("text");
            }
            return "";
        } catch (Exception e) {
            throw new RuntimeException("Failed to transcribe audio with Hugging Face", e);
        }
    }
}
