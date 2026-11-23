package com.vibo.GenAI.controller;

import com.vibo.GenAI.dto.ApiResponse;
import com.vibo.GenAI.dto.workspace.WorkspaceChatRequest;
import com.vibo.GenAI.dto.workspace.WorkspaceChatResponse;
import com.vibo.GenAI.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<WorkspaceChatResponse>> chat(@RequestBody WorkspaceChatRequest request) {
        try {
            WorkspaceChatResponse response = workspaceService.processWorkspaceChat(request);
            ApiResponse<WorkspaceChatResponse> apiResponse = new ApiResponse<>(200, "Success", response);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<WorkspaceChatResponse> errorResponse = new ApiResponse<>(500, e.getMessage(), null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/knowledge/upload")
    public ResponseEntity<ApiResponse<String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            String result = workspaceService.uploadDocument(file);
            ApiResponse<String> apiResponse = new ApiResponse<>(200, "Success", result);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, e.getMessage(), null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/history/chats")
    public ResponseEntity<ApiResponse<java.util.List<com.vibo.GenAI.entity.ChatHistory>>> getAllChats() {
        try {
            java.util.List<com.vibo.GenAI.entity.ChatHistory> chats = workspaceService.getAllChats();
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", chats));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, e.getMessage(), null));
        }
    }

    @GetMapping("/history/chats/{conversationId}/messages")
    public ResponseEntity<ApiResponse<java.util.List<com.vibo.GenAI.entity.ChatMessageHistory>>> getChatMessages(@PathVariable String conversationId) {
        try {
            java.util.List<com.vibo.GenAI.entity.ChatMessageHistory> messages = workspaceService.getChatMessages(conversationId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", messages));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, e.getMessage(), null));
        }
    }

    @GetMapping("/history/documents")
    public ResponseEntity<ApiResponse<java.util.List<com.vibo.GenAI.entity.DocumentUploadHistory>>> getDocumentHistory() {
        try {
            java.util.List<com.vibo.GenAI.entity.DocumentUploadHistory> docs = workspaceService.getDocumentHistory();
            return ResponseEntity.ok(new ApiResponse<>(200, "Success", docs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, e.getMessage(), null));
        }
    }
}
