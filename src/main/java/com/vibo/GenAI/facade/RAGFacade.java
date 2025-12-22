package com.vibo.GenAI.facade;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.service.RAGService;
import org.springframework.stereotype.Component;

@Component
public class RAGFacade {

    private final RAGService ragService;

    public RAGFacade(RAGService ragService) {
        this.ragService = ragService;
    }

    public ApiResponse<String> chatUsingRag(MessageDto dto) {
        try {
            String response = ragService.chatUsingRag(dto.getMessage());
            return new ApiResponse<>(200, "Responded successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }
}
