package com.vibo.GenAI.huggingface;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HuggingFaceImageModel implements ImageModel {

    private final RestClient restClient;
    private final String apiUrl;

    public HuggingFaceImageModel(String apiKey, String apiUrl) {
        this.apiUrl = apiUrl;
        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public ImageResponse call(ImagePrompt request) {
        String promptText = request.getInstructions().get(0).getText();
        Map<String, String> body = Map.of("inputs", promptText);

        byte[] imageBytes = restClient.post()
                .uri(apiUrl)
                .body(body)
                .retrieve()
                .body(byte[].class);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        Image image = new Image(null, base64Image);
        ImageGeneration imageGeneration = new ImageGeneration(image);

        return new ImageResponse(List.of(imageGeneration));
    }
}
