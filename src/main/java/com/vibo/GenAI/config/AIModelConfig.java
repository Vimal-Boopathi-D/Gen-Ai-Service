package com.vibo.GenAI.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AIModelConfig {

    private final OllamaChatModel ollamaChatModel;

    public AIModelConfig(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    @Bean
    public ChatClient chatClient(){

        ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();

        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }

    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate,EmbeddingModel embeddingModel){
        return PgVectorStore.builder(jdbcTemplate,embeddingModel)
                .dimensions(768)
                .build();
    }

}
