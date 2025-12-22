package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.facade.RAGFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class RAGController {
    private final RAGFacade ragFacade;

    public RAGController(RAGFacade ragFacade) {
        this.ragFacade = ragFacade;
    }

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<String>> chatUsingRag(@RequestBody MessageDto dto){

        ApiResponse<String> response = ragFacade.chatUsingRag(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
