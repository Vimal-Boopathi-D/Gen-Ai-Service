package com.vibo.GenAIPractice.facade;

import com.vibo.GenAIPractice.dto.ApiResponse;
import com.vibo.GenAIPractice.dto.MessageDto;
import com.vibo.GenAIPractice.service.ImageGenService;
import org.springframework.stereotype.Component;

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
}
