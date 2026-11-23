package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.dto.Response;
import com.vibo.GenAI.facade.AudioFacade;
import com.vibo.GenAI.facade.ChatFacade;
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
    private final ChatFacade chatFacade;

    public AudioGenController(AudioFacade audioFacade, ChatFacade chatFacade) {
        this.audioFacade = audioFacade;
        this.chatFacade = chatFacade;
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

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Response>> audioToChat(@RequestBody MultipartFile file){
        ApiResponse<String> textResponse = audioFacade.audioToText(file);
        if(textResponse.getStatusCode() != 200 || textResponse.getData() == null) {
            return ResponseEntity.status(textResponse.getStatusCode())
                    .body(new ApiResponse<>(textResponse.getStatusCode(), textResponse.getMessage(), null));
        }
        
        MessageDto dto = new MessageDto();
        dto.setMessage((String) textResponse.getData());
        
        ApiResponse<Response> chatResponse = chatFacade.chat(dto);
        return ResponseEntity.status(chatResponse.getStatusCode()).body(chatResponse);
    }
}
