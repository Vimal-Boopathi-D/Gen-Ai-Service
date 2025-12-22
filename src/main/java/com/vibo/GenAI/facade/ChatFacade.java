package com.vibo.GenAI.facade;

import com.vibo.GenAI.dto.*;
import com.vibo.GenAI.service.ChatService;
import com.vibo.GenAI.service.RAGService;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class ChatFacade {

    private final ChatService chatService;
    private final RAGService ragService;

    public ChatFacade(ChatService chatService, RAGService ragService) {
        this.chatService = chatService;
        this.ragService = ragService;
    }

    public ApiResponse<Response> chat(MessageDto message) {
      try{
        Response response = chatService.chat(message);
        return new ApiResponse<>(200, "responded successfully", response);
    } catch (Exception e) {
        return new ApiResponse<>(409,e.getMessage(),null);
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

}
