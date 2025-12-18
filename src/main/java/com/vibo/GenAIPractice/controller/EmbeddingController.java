package com.vibo.GenAIPractice.controller;

import com.vibo.GenAIPractice.dto.ApiResponse;
import com.vibo.GenAIPractice.facade.EmbeddingFacade;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class EmbeddingController {
    private final EmbeddingFacade embeddingFacade;

    public EmbeddingController(EmbeddingFacade embeddingFacade) {
        this.embeddingFacade = embeddingFacade;
    }

    @GetMapping("/embed")
    public ResponseEntity<ApiResponse<float[]>>embedding(@RequestParam String text){
        ApiResponse<float[]> embeddings= embeddingFacade.embedding(text);
        return ResponseEntity.status(embeddings.getStatusCode()).body(embeddings);
    }

    @GetMapping("/similarity")
    public ResponseEntity<ApiResponse<Double>>cosineSimilarity(@RequestParam String text1,@RequestParam String text2){
        ApiResponse<Double> similarity= embeddingFacade.cosineSimilarity(text1,text2);
        return ResponseEntity.status(similarity.getStatusCode()).body(similarity);
    }

    @PostMapping("similar/products")
    public ResponseEntity<ApiResponse<List<Document>>> semanticSearch(@RequestParam String text){
        ApiResponse<List<Document>> products= embeddingFacade.getSimilarProducts(text);
        return ResponseEntity.status(products.getStatusCode()).body(products);
    }

}
