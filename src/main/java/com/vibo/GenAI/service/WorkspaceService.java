package com.vibo.GenAI.service;

import com.vibo.GenAI.dto.workspace.WorkspaceChatRequest;
import com.vibo.GenAI.dto.workspace.WorkspaceChatResponse;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface WorkspaceService {
    WorkspaceChatResponse processWorkspaceChat(WorkspaceChatRequest request);
    String uploadDocument(MultipartFile file);
    List<com.vibo.GenAI.entity.ChatHistory> getAllChats();
    List<com.vibo.GenAI.entity.ChatMessageHistory> getChatMessages(String conversationId);
    List<com.vibo.GenAI.entity.DocumentUploadHistory> getDocumentHistory();
}
