package com.vibo.GenAI.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AudioService {
    String audioToText(MultipartFile file);

    byte[] textToAudio(String text);
}
