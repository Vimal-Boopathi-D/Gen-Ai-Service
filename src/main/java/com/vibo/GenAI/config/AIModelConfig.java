package com.vibo.GenAI.config;

import com.vibo.GenAI.huggingface.HuggingFaceAudioSpeechModel;
import com.vibo.GenAI.huggingface.HuggingFaceAudioTranscriptionModel;
import com.vibo.GenAI.huggingface.HuggingFaceImageModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AIModelConfig {

    private final ChatModel chatModel;

    @Value("${huggingface.api-key}")
    private String apiKey;

    public AIModelConfig(@Qualifier("openAiChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Bean
    public ChatClient chatClient() {

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();

        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build()
                )
                .build();
    }

    @Bean
    public VectorStore vectorStore(
            JdbcTemplate jdbcTemplate,
            EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(768)
                .build();
    }

    @Bean
    public ImageModel huggingFaceImageModel() {
        String apiUrl = "https://router.huggingface.co/hf-inference/models/stabilityai/stable-diffusion-3-medium-diffusers";
        return new HuggingFaceImageModel(apiKey, apiUrl);
    }

    @Bean
    public HuggingFaceAudioTranscriptionModel huggingFaceAudioTranscriptionModel() {
        String apiUrl = "https://router.huggingface.co/hf-inference/models/openai/whisper-large-v3";
        return new HuggingFaceAudioTranscriptionModel(apiKey, apiUrl);
    }

    @Bean
    public HuggingFaceAudioSpeechModel huggingFaceSpeechModel() {
        String apiUrl = "https://router.huggingface.co/hf-inference/models/facebook/mms-tts-eng";
        return new HuggingFaceAudioSpeechModel(apiKey, apiUrl);
    }
}