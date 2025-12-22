package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.facade.ImageGenFacade;
import com.vibo.GenAI.service.ImageGenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class ImageGenController {
    private final ImageGenFacade imageGenFacade;
    private final ImageGenService imageGenService;

    public ImageGenController(ImageGenFacade imageGenFacade, ImageGenService imageGenService) {
        this.imageGenFacade = imageGenFacade;
        this.imageGenService = imageGenService;
    }

    @PostMapping("/genImage")
    public ResponseEntity<ApiResponse<String>> generateImage(@RequestBody MessageDto dto){

        ApiResponse<String> response= imageGenFacade.genImage(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("image/analyze")
    public ResponseEntity<ApiResponse<String>> analyzeImage(@RequestParam String query, @RequestBody MultipartFile file){
        ApiResponse<String> response =imageGenFacade.analyzeImage(query,file);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
