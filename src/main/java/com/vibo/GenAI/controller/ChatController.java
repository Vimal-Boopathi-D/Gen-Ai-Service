package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.*;
import com.vibo.GenAI.facade.ChatFacade;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatFacade chatFacade;

    public ChatController(ChatFacade chatFacade) { // for single model use Builder
        this.chatFacade = chatFacade;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Response>> chat(@RequestBody MessageDto message) {

        ApiResponse<Response> response = chatFacade.chat(message);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/chat/image")
    public ResponseEntity<ApiResponse<Response>> chatWithImage(
            @RequestParam("message") String message,
            @RequestPart("file") MultipartFile file) {

        ApiResponse<Response> response = chatFacade.chatWithImage(message, file);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/recommend/course")
    public ResponseEntity<ApiResponse<CourseRecomendResponseDto>> recommendMovie(@RequestBody CourseRecomendRequestDto courseRecomendRequestDto) {

        ApiResponse<CourseRecomendResponseDto> response = chatFacade.recommendCourse(courseRecomendRequestDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<String>> message(@RequestBody MessageDto dto) {

        ApiResponse<String> response = chatFacade.routeMessage(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/list/movies")
    public ResponseEntity<ApiResponse<List<String>>> listOutputConverting(@RequestBody MessageDto messageDto) {

        ApiResponse<List<String>> response = chatFacade.listOutputConverting(messageDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/bean/movies")
    public ResponseEntity<ApiResponse<Movie>> beanOutputConverting(@RequestBody MessageDto messageDto) {
        ApiResponse<Movie> response = chatFacade.beanOutputConverting(messageDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("list/bean/movies")
    public ResponseEntity<ApiResponse<List<Movie>>> listOfBeanOutputConverting(@RequestBody MessageDto messageDto) {
        ApiResponse<List<Movie>> response = chatFacade.listOfBeanOutputConverting(messageDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
