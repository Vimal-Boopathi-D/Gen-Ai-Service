package com.vibo.GenAIPractice.service.impl;

import com.vibo.GenAIPractice.service.RAGService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RAGServiceImpl implements RAGService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RAGServiceImpl(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Override
    public String chatUsingRag(String message) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor
                .builder(vectorStore)
                .build();

        return chatClient.prompt()
                .user(message)
                .advisors(advisor)
                .call()
                .content();
    }
}
