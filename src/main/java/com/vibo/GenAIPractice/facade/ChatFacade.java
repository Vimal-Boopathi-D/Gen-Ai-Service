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

    public ApiResponse<Response> chat(MessageDto message){
        Response response = chatService.chat(message);
        return new ApiResponse<>(200,"responded successfully",response);
    }

    public ApiResponse<CourseRecomendResponseDto> recommendCourse(CourseRecomendRequestDto courseRecomendRequestDto){
        CourseRecomendResponseDto response = chatService.recommendCourse(courseRecomendRequestDto);
        return new ApiResponse<>(200,"recommended successfully",response);
    }
}
