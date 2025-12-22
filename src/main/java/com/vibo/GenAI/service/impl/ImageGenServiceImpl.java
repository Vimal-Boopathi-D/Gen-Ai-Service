package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.ImageGenService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.*;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageGenServiceImpl implements ImageGenService {
    private final ImageModel imageModel;
    private final ChatClient chatClient;

    public ImageGenServiceImpl(ImageModel imageModel, ChatClient chatClient) {
        this.imageModel = imageModel;
        this.chatClient = chatClient;
    }

    @Override
    public String genImage(String message) {

            ImagePrompt imagePrompt = new ImagePrompt(message,
                    OpenAiImageOptions.builder()
                            .quality("hd")
                            .height(1024)
                            .width(1024)
                            .style("natural")
                            .build()
            );
            ImageResponse response = imageModel.call(imagePrompt);
            return response.getResult().getOutput().getUrl();

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
