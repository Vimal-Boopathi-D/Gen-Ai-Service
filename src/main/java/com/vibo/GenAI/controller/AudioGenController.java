package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.facade.AudioFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai/audio")
public class AudioGenController {

    private final AudioFacade audioFacade;

    public AudioGenController(AudioFacade audioFacade) {
        this.audioFacade = audioFacade;
    }

    @PostMapping("/att")
    public ResponseEntity<ApiResponse<String>> audioToText(@RequestBody MultipartFile file){
        ApiResponse<String> response = audioFacade.audioToText(file);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("tta")
    public ResponseEntity<ApiResponse<byte[]>> textToAudio(@RequestBody String text){
        ApiResponse<byte[]> audioResponse = audioFacade.textToAudio(text);
        return ResponseEntity.status(audioResponse.getStatusCode()).body(audioResponse);
    }
}
