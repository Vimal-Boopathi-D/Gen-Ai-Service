package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.ImageGenService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageGenServiceImpl implements ImageGenService {
    private final ImageModel imageModel;
    private final ChatClient chatClient;

    public ImageGenServiceImpl(@Qualifier("huggingFaceImageModel") ImageModel imageModel, ChatClient chatClient) {
        this.imageModel = imageModel;
        this.chatClient = chatClient;
    }

    @Override
    public String genImage(String message) {

            ImagePrompt imagePrompt = new ImagePrompt(message);
            ImageResponse response = imageModel.call(imagePrompt);
            return response.getResult().getOutput().getB64Json();

    }

    @Override
    public String analyzeImage(String query, MultipartFile file) {
        if (file == null){
            throw new IllegalArgumentException("image is required");
        }
        return chatClient.prompt()
                .user(us->
                        us.text(query)
                          .media(MimeTypeUtils.IMAGE_JPEG,file.getResource()))
                .call()
                .content();
    }


}
