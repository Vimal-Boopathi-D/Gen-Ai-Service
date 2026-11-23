package com.vibo.GenAI.repository;

import com.vibo.GenAI.entity.ChatMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageHistoryRepository extends JpaRepository<ChatMessageHistory, Long> {
    List<ChatMessageHistory> findByConversationIdOrderByCreatedAtAsc(String conversationId);
}
