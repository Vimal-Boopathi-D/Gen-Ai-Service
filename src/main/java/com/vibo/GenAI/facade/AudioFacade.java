package com.vibo.GenAI.facade;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.service.AudioService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AudioFacade {
    private final AudioService audioService;

    public AudioFacade(AudioService audioService) {
        this.audioService = audioService;
    }

    public ApiResponse<String> audioToText(MultipartFile file) {
       try {
           String response = audioService.audioToText(file);
           return new ApiResponse<>(200, "converted to text", response);
       } catch (Exception e) {
           return new ApiResponse<>(409, e.getMessage(), null);
       }
    }

    public ApiResponse<byte[]> textToAudio(String text) {
        try {
            byte[] audio = audioService.textToAudio(text);
            return new ApiResponse<>(200,"converted to audio",audio);
        } catch (Exception e) {
            return new ApiResponse<>(409, e.getMessage(), null);
        }
    }
}
