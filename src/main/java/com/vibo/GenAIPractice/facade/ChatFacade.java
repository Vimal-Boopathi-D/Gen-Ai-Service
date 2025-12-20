package com.vibo.GenAIPractice.facade;

import com.vibo.GenAIPractice.dto.*;
import com.vibo.GenAIPractice.service.ChatService;
import org.springframework.stereotype.Component;


@Component
public class ChatFacade {

    private final ChatService chatService;

    public ChatFacade(ChatService chatService) {
        this.chatService = chatService;
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

}
