package com.vibo.GenAI.facade;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.MessageDto;
import com.vibo.GenAI.service.ImageGenService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageGenFacade {
    private final ImageGenService imageGenService;

    public ImageGenFacade(ImageGenService imageGenService) {
        this.imageGenService = imageGenService;
    }

    public ApiResponse<String> genImage(MessageDto dto) {
        try {
            String url = imageGenService.genImage(dto.getMessage());
            return new ApiResponse<>(200,"Url generated successfully",url);

        } catch (Exception e){
            return new ApiResponse<>(402,e.getMessage(),null);
        }
    }

    public ApiResponse<String> analyzeImage(String query, MultipartFile file) {
        try{
            String response = imageGenService.analyzeImage(query,file);
            return new ApiResponse<>(200,"described successfully",response);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }

}
