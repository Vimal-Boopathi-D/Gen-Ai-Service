package com.vibo.GenAI.huggingface;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import java.util.Map;

public class HuggingFaceAudioSpeechModel {

    private final RestClient restClient;
    private final String apiUrl;

    public HuggingFaceAudioSpeechModel(String apiKey, String apiUrl) {
        this.apiUrl = apiUrl;
        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public byte[] generateSpeech(String text) {
        Map<String, String> body = Map.of("inputs", text);

        return restClient.post()
                .uri(apiUrl)
                .body(body)
                .retrieve()
                .body(byte[].class);
    }
}
