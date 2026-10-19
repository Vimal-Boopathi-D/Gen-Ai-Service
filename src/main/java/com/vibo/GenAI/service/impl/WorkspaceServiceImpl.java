package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.dto.workspace.SourceMetadata;
import com.vibo.GenAI.dto.workspace.WorkspaceChatRequest;
import com.vibo.GenAI.dto.workspace.WorkspaceChatResponse;
import com.vibo.GenAI.service.WorkspaceService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final com.vibo.GenAI.repository.DocumentUploadHistoryRepository uploadHistoryRepo;
    private final com.vibo.GenAI.repository.ChatHistoryRepository chatHistoryRepo;
    private final com.vibo.GenAI.repository.ChatMessageHistoryRepository chatMessageHistoryRepo;

    public WorkspaceServiceImpl(ChatClient chatClient, 
                                VectorStore vectorStore, 
                                com.vibo.GenAI.repository.DocumentUploadHistoryRepository uploadHistoryRepo,
                                com.vibo.GenAI.repository.ChatHistoryRepository chatHistoryRepo,
                                com.vibo.GenAI.repository.ChatMessageHistoryRepository chatMessageHistoryRepo) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.uploadHistoryRepo = uploadHistoryRepo;
        this.chatHistoryRepo = chatHistoryRepo;
        this.chatMessageHistoryRepo = chatMessageHistoryRepo;
    }

    @Override
    public WorkspaceChatResponse processWorkspaceChat(WorkspaceChatRequest request) {
        String mode = request.getMode() != null ? request.getMode().toUpperCase() : "AUTO";
        String message = request.getMessage();
        
        WorkspaceChatResponse response = new WorkspaceChatResponse();

        if ("AUTO".equals(mode)) {
            // Very simple router: if message contains specific keywords, use KNOWLEDGE, else CHAT
            // In a real scenario, this would use a router LLM call, but for simplicity here:
            if (message.toLowerCase().contains("policy") || message.toLowerCase().contains("handbook") || 
                message.toLowerCase().contains("document") || message.toLowerCase().contains("our")) {
                mode = "KNOWLEDGE";
            } else {
                mode = "CHAT";
            }
        }

        response.setModeUsed(mode);

        if ("KNOWLEDGE".equals(mode)) {
            // 1. Search vector store
            List<Document> documents = vectorStore.similaritySearch(
                    SearchRequest.builder().query(message).topK(5).build()
            );

            // 2. Map documents to SourceMetadata
            List<SourceMetadata> sources = new ArrayList<>();
            StringBuilder contextBuilder = new StringBuilder();
            
            for (Document doc : documents) {
                SourceMetadata meta = new SourceMetadata();
                // Extract metadata if available, fallback to defaults
                Object docName = doc.getMetadata().get("document");
                meta.setDocument(docName != null ? docName.toString() : "Unknown Document");
                
                Object page = doc.getMetadata().get("page");
                if (page instanceof Integer) {
                    meta.setPage((Integer) page);
                } else if (page != null) {
                    try { meta.setPage(Integer.parseInt(page.toString())); } catch (Exception e) {}
                }
                
                meta.setContent(doc.getText());
                sources.add(meta);
                
                contextBuilder.append("Document: ").append(meta.getDocument()).append("\n");
                contextBuilder.append("Content: ").append(doc.getText()).append("\n\n");
            }
            response.setSources(sources);

            // 3. Generate response with context
            String template = """
                    You are a helpful enterprise AI assistant.
                    Answer the user's question based ONLY on the following context.
                    If the answer is not in the context, say "I don't have enough information to answer that based on the knowledge base."
                    
                    Context:
                    {context}
                    """;
            
            PromptTemplate promptTemplate = new PromptTemplate(template);
            promptTemplate.add("context", contextBuilder.toString());
            
            String answer = chatClient.prompt(promptTemplate.create())
                    .user(message)
                    .call()
                    .content();
                    
            response.setAnswer(answer);
            
        } else {
            // Normal CHAT mode
            String template = """
                    You are Vibo AI, a helpful enterprise AI assistant.
                    Answer the user's question clearly and concisely.
                    """;
            PromptTemplate promptTemplate = new PromptTemplate(template);
            
            String answer = chatClient.prompt(promptTemplate.create())
                    .user(message)
                    .call()
                    .content();
                    
            response.setAnswer(answer);
        }

        // Save History
        if (request.getConversationId() != null && !request.getConversationId().trim().isEmpty()) {
            String cid = request.getConversationId();
            
            // Optionally save/update the parent ChatHistory if needed, assuming the frontend generates cid
            // You can query chatHistoryRepo here and create if it doesn't exist.
            java.util.List<com.vibo.GenAI.entity.ChatMessageHistory> existing = chatMessageHistoryRepo.findByConversationIdOrderByCreatedAtAsc(cid);
            if (existing.isEmpty()) {
                com.vibo.GenAI.entity.ChatHistory chatHistory = new com.vibo.GenAI.entity.ChatHistory();
                chatHistory.setConversationId(cid);
                chatHistory.setMode(mode);
                chatHistory.setTitle(message.length() > 50 ? message.substring(0, 50) + "..." : message);
                chatHistoryRepo.save(chatHistory);
            }
            
            com.vibo.GenAI.entity.ChatMessageHistory userMsg = new com.vibo.GenAI.entity.ChatMessageHistory();
            userMsg.setConversationId(cid);
            userMsg.setRole("user");
            userMsg.setContent(message);
            chatMessageHistoryRepo.save(userMsg);
            
            com.vibo.GenAI.entity.ChatMessageHistory aiMsg = new com.vibo.GenAI.entity.ChatMessageHistory();
            aiMsg.setConversationId(cid);
            aiMsg.setRole("assistant");
            aiMsg.setContent(response.getAnswer());
            chatMessageHistoryRepo.save(aiMsg);
        }

        return response;
    }

    @Override
    public String uploadDocument(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) filename = "unknown";
            
            // Save the document to root 'documents' folder
            java.io.File documentsDir = new java.io.File("documents");
            if (!documentsDir.exists()) {
                documentsDir.mkdirs();
            }
            java.nio.file.Path targetPath = documentsDir.toPath().resolve(filename);
            file.transferTo(targetPath.toFile());
            
            Resource resource = new org.springframework.core.io.FileSystemResource(targetPath.toFile());
            List<Document> documents;
            
            if (filename.toLowerCase().endsWith(".pdf")) {
                PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
                documents = reader.get();
            } else {
                TextReader reader = new TextReader(resource);
                // Add basic metadata
                reader.getCustomMetadata().put("document", filename);
                documents = reader.get();
            }
            
            // Add document name metadata to all docs if not present
            for (int i = 0; i < documents.size(); i++) {
                Document doc = documents.get(i);
                doc.getMetadata().putIfAbsent("document", filename);
                doc.getMetadata().putIfAbsent("page", i + 1);
            }
            
            // Split documents
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> splitDocs = splitter.apply(documents);
            
            vectorStore.add(splitDocs);
            
            com.vibo.GenAI.entity.DocumentUploadHistory history = new com.vibo.GenAI.entity.DocumentUploadHistory();
            history.setFilename(filename);
            history.setChunks(splitDocs.size());
            history.setStatus("SUCCESS");
            history.setSavedPath(targetPath.toString());
            uploadHistoryRepo.save(history);
            
            return "Successfully uploaded and indexed " + filename + " into " + splitDocs.size() + " chunks.";
        } catch (Exception e) {
            throw new RuntimeException("Failed to process document: " + e.getMessage());
        }
    }

    @Override
    public java.util.List<com.vibo.GenAI.entity.ChatHistory> getAllChats() {
        return chatHistoryRepo.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public java.util.List<com.vibo.GenAI.entity.ChatMessageHistory> getChatMessages(String conversationId) {
        return chatMessageHistoryRepo.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Override
    public java.util.List<com.vibo.GenAI.entity.DocumentUploadHistory> getDocumentHistory() {
        return uploadHistoryRepo.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "uploadedAt"));
    }
}
