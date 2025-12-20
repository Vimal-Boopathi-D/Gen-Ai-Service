package com.vibo.GenAIPractice.facade;

import com.vibo.GenAIPractice.dto.ApiResponse;
import com.vibo.GenAIPractice.service.EmbeddingService;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmbeddingFacade {
    private final EmbeddingService embeddingService;

    public EmbeddingFacade(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    public ApiResponse<float[]> embedding(String text){
        try {
            float[] embeddings=embeddingService.embedding(text);
            return new ApiResponse<>(200,"embedded successfully",embeddings);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }

    }

    public ApiResponse<Double> cosineSimilarity(String text1, String text2){

        try {
            double similarity = embeddingService.cosineSimilarity(text1, text2) * 100;
            if (similarity >= 90.0) {
                return new ApiResponse<>(200, "words are almost similar", similarity);
            }

            if (similarity < 90.0 && similarity >= 75) {
                return new ApiResponse<>(200, "words are very similar", similarity);
            }

            if (similarity < 75.0 && similarity > 50.0) {
                return new ApiResponse<>(200, "words are moderately similar", similarity);
            }

            if (similarity < 50.0 && similarity >= 0.0) {
                return new ApiResponse<>(200, "words are not similar", similarity);
            }
            return new ApiResponse<>(200, "similarity is calculated", similarity);

        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }
    }

    public ApiResponse<List<Document>> getSimilarProducts(String text) {
        try{
            List<Document> products = embeddingService.getSimilarProducts(text);
            return new ApiResponse<>(200,"products fetched successfully",products);
        } catch (Exception e) {
            return new ApiResponse<>(409,e.getMessage(),null);
        }

    }
}
