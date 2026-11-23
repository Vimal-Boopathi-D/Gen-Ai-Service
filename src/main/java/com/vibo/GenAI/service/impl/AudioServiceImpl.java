package com.vibo.GenAI.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.genai.types.PrebuiltVoiceConfig;
import com.google.genai.types.SpeechConfig;
import com.google.genai.types.VoiceConfig;
import com.vibo.GenAI.service.AudioService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AudioServiceImpl implements AudioService {

    private final ChatClient chatClient;
    private final Client genAiClient;

    public AudioServiceImpl(ChatClient chatClient, Client genAiClient) {
        this.chatClient = chatClient;
        this.genAiClient = genAiClient;
    }

    @Override
    public String audioToText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Audio file is required");
        }
        MimeType mimeType = resolveMimeType(file);
        String prompt = "Transcribe the spoken audio recording accurately into text. Output only the transcribed text with no markdown, preamble, or commentary.";

        String response = chatClient.prompt()
                .user(u -> u.text(prompt)
                            .media(mimeType, file.getResource()))
                .call()
                .content();

        if (response == null || response.isBlank()) {
            throw new RuntimeException("Failed to transcribe audio from Gemini model");
        }
        return response.trim();
    }

    @Override
    public byte[] textToAudio(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text is required");
        }
        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(List.of("AUDIO"))
                    .speechConfig(SpeechConfig.builder()
                            .voiceConfig(VoiceConfig.builder()
                                    .prebuiltVoiceConfig(PrebuiltVoiceConfig.builder()
                                            .voiceName("Puck")
                                            .build())
                                    .build())
                            .build())
                    .build();

            GenerateContentResponse response = genAiClient.models.generateContent(
                    "gemini-2.0-flash",
                    "Read the following text aloud clearly: " + text,
                    config
            );

            if (response.parts() != null) {
                for (Part part : response.parts()) {
                    if (part.inlineData().isPresent() && part.inlineData().get().data().isPresent()) {
                        return part.inlineData().get().data().get();
                    }
                }
            }
            throw new RuntimeException("No audio content returned by Gemini");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate audio from Gemini: " + e.getMessage(), e);
        }
    }

    private MimeType resolveMimeType(MultipartFile file) {
        try {
            if (file.getContentType() != null && !file.getContentType().isBlank()) {
                return MimeType.valueOf(file.getContentType());
            }
        } catch (Exception ignored) {
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String lower = originalFilename.toLowerCase();
            if (lower.endsWith(".mp3")) {
                return MimeType.valueOf("audio/mp3");
            } else if (lower.endsWith(".wav")) {
                return MimeType.valueOf("audio/wav");
            } else if (lower.endsWith(".ogg")) {
                return MimeType.valueOf("audio/ogg");
            } else if (lower.endsWith(".m4a")) {
                return MimeType.valueOf("audio/m4a");
            }
        }
        return MimeTypeUtils.parseMimeType("audio/wav");
    }
}
