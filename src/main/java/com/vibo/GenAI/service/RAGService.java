package com.vibo.GenAI.service;

import org.springframework.stereotype.Component;

@Component
public interface RAGService {
    String chatUsingRag(String message);
}
