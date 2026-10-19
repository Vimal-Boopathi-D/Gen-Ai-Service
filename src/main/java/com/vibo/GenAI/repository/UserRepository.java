package com.vibo.GenAI.repository;

import com.vibo.GenAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
