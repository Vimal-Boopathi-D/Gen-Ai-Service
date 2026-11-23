package com.vibo.GenAI.repository;

import com.vibo.GenAI.entity.DocumentUploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentUploadHistoryRepository extends JpaRepository<DocumentUploadHistory, Long> {
}
