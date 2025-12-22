package com.vibo.GenAI.service;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EmbeddingService {
    float[] embedding(String text);
    double cosineSimilarity(String text1, String text2);
    List<Document> getSimilarProducts(String text);
}
