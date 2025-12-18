package com.vibo.GenAIPractice.controller;

import com.vibo.GenAIPractice.dto.*;
import com.vibo.GenAIPractice.facade.ChatFacade;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatFacade chatFacade;

    ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();

    public ChatController(ChatFacade chatFacade) { // for single model use Builder
        this.chatFacade = chatFacade;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Response>> chat(@RequestBody MessageDto  message){

        ApiResponse<Response> response=chatFacade.chat(message);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/recommend/course")
    public ResponseEntity<ApiResponse<CourseRecomendResponseDto>> recommendMovie(@RequestBody CourseRecomendRequestDto courseRecomendRequestDto){

        ApiResponse<CourseRecomendResponseDto> response=chatFacade.recommendCourse(courseRecomendRequestDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
