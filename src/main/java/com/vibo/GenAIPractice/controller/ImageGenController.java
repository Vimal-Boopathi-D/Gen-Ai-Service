package com.vibo.GenAIPractice.controller;

import com.vibo.GenAIPractice.dto.ApiResponse;
import com.vibo.GenAIPractice.dto.MessageDto;
import com.vibo.GenAIPractice.facade.ImageGenFacade;
import com.vibo.GenAIPractice.service.ImageGenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String prompt) {
        String base64Image = imageGenService.genImage(prompt);
        return ResponseEntity.ok(base64Image);
    }
}
