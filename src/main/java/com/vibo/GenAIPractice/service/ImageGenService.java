package com.vibo.GenAIPractice.service;

import org.springframework.stereotype.Component;

@Component
public interface ImageGenService {
    String genImage(String message);
    String genImageUsingWebClient(String prompt);
}
