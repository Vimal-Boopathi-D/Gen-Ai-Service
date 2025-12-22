package com.vibo.GenAI.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface ImageGenService {
    String genImage(String message);

    String analyzeImage(String query, MultipartFile file);
}
