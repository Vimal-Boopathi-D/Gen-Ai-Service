package com.vibo.GenAI.facade;

import com.vibo.GenAI.dto.*;
import com.vibo.GenAI.service.AudioService;
import com.vibo.GenAI.service.ChatService;
import com.vibo.GenAI.service.ImageGenService;
import com.vibo.GenAI.service.RAGService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class ChatFacade {

    private final ChatService chatService;
    private final RAGService ragService;
    private final ImageGenService imageGenService;
    private final AudioService audioService;

    public ChatFacade(ChatService chatService, RAGService ragService, ImageGenService imageGenService, AudioService audioService) {
        this.chatService = chatService;
        this.ragService = ragService;
        this.imageGenService = imageGenService;
        this.audioService = audioService;
    }

    public ApiResponse<Response> chat(MessageDto dto) {
      try{
        String msg = dto.getMessage().toLowerCase();
        Response response = new Response();
        
        if (msg.contains("generate an image") || msg.contains("create an image") || msg.contains("draw") || msg.contains("picture of") || msg.contains("image")) {
            String b64 = imageGenService.genImage(dto.getMessage());
            response.setResponse("IMAGE_BASE64:" + b64);
        } else if (msg.contains("generate audio") || msg.contains("speak") || msg.contains("say") || msg.contains("text to speech")) {
            String textToSpeak = dto.getMessage().replaceFirst("(?i)(generate audio|speak|say|text to speech)\\s*(of|about|for)?\\s*", "");
            byte[] audioBytes = audioService.textToAudio(textToSpeak.trim().isEmpty() ? dto.getMessage() : textToSpeak.trim());
            String b64 = java.util.Base64.getEncoder().encodeToString(audioBytes);
            response.setResponse("AUDIO_BASE64:" + b64);
        } else {
            response = chatService.chat(dto);
        }
        return new ApiResponse<>(200, "responded successfully", response);
    } catch (Exception e) {
        return new ApiResponse<>(409,e.getMessage(),null);
        }
    }

    public ApiResponse<Response> chatWithImage(String message, org.springframework.web.multipart.MultipartFile file) {
        try {
            String result = imageGenService.analyzeImage(message, file);
            Response response = new Response();
            response.setResponse(result);
            return new ApiResponse<>(200, "responded successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>(409, e.getMessage(), null);
        }
    }

    public ApiResponse<CourseRecomendResponseDto> recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto){

       try {
           CourseRecomendResponseDto response = chatService.recommendCourse(courseRecomendRequestDto);
           return new ApiResponse<>(200, "recommended successfully", response);
       } catch (Exception e) {
           return new ApiResponse<>(409,e.getMessage(),null);
       }
    }


    public ApiResponse<String> routeMessage(MessageDto dto) {
        String message = dto.getMessage().toLowerCase();
        boolean isProductQuery = isProductRelated(message);
        if (isProductQuery) {
            String result = ragService.chatUsingRag(dto.getMessage());
            return new ApiResponse<>(200, "responded successfully", result);
        } else {
            String result = chatService.chat(dto).getResponse();
            return new ApiResponse<>(200, "responded successfully", result);
        }
    }


    private static final Set<String> PRODUCT_KEYWORDS = Set.of(
            "mobile",
            "phone",
            "price",
            "repair",
            "service",
            "laptop",
            "cctv",
            "shop",
            "store",
            "warranty",
            "location",
            "about",
            "mobo"
    );

    private boolean isProductRelated(String message) {
        if (message == null) return false;

        String lowerMsg = message.toLowerCase();

        return PRODUCT_KEYWORDS.stream()
                .anyMatch(lowerMsg::contains);
    }

    public ApiResponse<List<String>> listOutputConverting(MessageDto messageDto) {
        try {
            List<String> response = chatService.listOpConverting(messageDto.getMessage());
            return new ApiResponse<>(200,"responsed successfully",response);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }

    public ApiResponse<Movie> beanOutputConverting(MessageDto messageDto) {
        try {
            Movie response = chatService.beanOpConverting(messageDto.getMessage());
            return new ApiResponse<>(200,"responsed successfully",response);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }

    public ApiResponse<List<Movie>> listOfBeanOutputConverting(MessageDto messageDto) {
        try {
            List<Movie> response = chatService.listOfBeanOpConverting(messageDto.getMessage());
            return new ApiResponse<>(200,"responsed successfully",response);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }
}
