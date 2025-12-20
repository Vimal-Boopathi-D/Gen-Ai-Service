package com.vibo.GenAIPractice.service.impl;

import com.vibo.GenAIPractice.dto.ApiResponse;
import com.vibo.GenAIPractice.dto.SdRequest;
import com.vibo.GenAIPractice.dto.SdResponse;
import com.vibo.GenAIPractice.service.ImageGenService;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ImageGenServiceImpl implements ImageGenService {
    private final ImageModel imageModel;
    private final WebClient webClient;


    public ImageGenServiceImpl(ImageModel imageModel,WebClient.Builder builder) {
        this.imageModel = imageModel;
        this.webClient = builder
                .baseUrl("http://localhost:7860")
                .build();    }

    @Override
    public String genImage(String message) {
            ImagePrompt imagePrompt = new ImagePrompt(message);
            ImageResponse response = imageModel.call(imagePrompt);
            return response.getResult().getOutput().getUrl();

    }

    @Override
    public String genImageUsingWebClient(String prompt) {

        SdRequest request = new SdRequest();
        request.setPrompt(prompt);

        SdResponse response = webClient.post()
                .uri("/sdapi/v1/txt2img")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SdResponse.class)
                .block();

        return response.getImages().get(0);
    }
}
