package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.RAGService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
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

        String template = """
                keep the response in short for chat bot
                Response format:
                ->product title
                -->price
                -->features
                -->description
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create();
        return chatClient.prompt(prompt)
                .user(message)
                .advisors(advisor)
                .call()
                .content();
    }
}
