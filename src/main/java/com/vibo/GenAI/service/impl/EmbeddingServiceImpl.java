package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.EmbeddingService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public EmbeddingServiceImpl(EmbeddingModel embeddingModel, VectorStore vectorStore) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public float[] embedding(String text){
        return embeddingModel.embed(text);
    }

    public double cosineSimilarity(String text1, String text2){
        float[] embeddings1=embeddingModel.embed(text1);
        float[] embeddings2=embeddingModel.embed(text2);

        double dotProduct = 0.0;
        double num1 =0.0;
        double num2 = 0.0;


//        cosineSimilarity = division of A vector.B vector by
//        square root of square of A vector.square of B vector
//        for multidimensional use summation so here for loop is used

        for (int i=0;i<embeddings1.length;i++){
            dotProduct += embeddings1[i]*embeddings2[i];
            num1 += Math.pow(embeddings1[i],2);
            num2 += Math.pow(embeddings2[i],2);
        }

        return dotProduct/(Math.sqrt(num1) * Math.sqrt(num2));
    }

    @Override
    public List<Document> getSimilarProducts(String text) {
        return vectorStore.similaritySearch(text);
    }
}
