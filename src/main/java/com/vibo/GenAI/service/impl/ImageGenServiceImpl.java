package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.ImageGenService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageGenServiceImpl implements ImageGenService {
    private final ImageModel imageModel;
    private final ChatClient chatClient;

    public ImageGenServiceImpl(@Qualifier("imageModel") ImageModel imageModel, ChatClient chatClient) {
        this.imageModel = imageModel;
        this.chatClient = chatClient;
    }

    @Override
    public String genImage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt message is required");
        }
        ImagePrompt imagePrompt = new ImagePrompt(message);
        ImageResponse response = imageModel.call(imagePrompt);
        if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
            throw new RuntimeException("Failed to generate image from model");
        }
        Image output = response.getResult().getOutput();
        return output.getUrl() != null ? output.getUrl() : output.getB64Json();
    }

    @Override
    public String analyzeImage(String query, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }
        final MimeType mimeType = resolveMimeType(file);
        return chatClient.prompt()
                .user(us ->
                        us.text(query)
                          .media(mimeType, file.getResource()))
                .call()
                .content();
    }

    private MimeType resolveMimeType(MultipartFile file) {
        try {
            if (file.getContentType() != null && !file.getContentType().isBlank()) {
                return MimeType.valueOf(file.getContentType());
            }
        } catch (Exception ignored) {
        }
        return MimeTypeUtils.IMAGE_JPEG;
    }
}
