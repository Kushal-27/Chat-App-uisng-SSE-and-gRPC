package com.example.repository;

import com.example.entity.ChatEntity;
import com.example.entity.UserEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
@Repository
public interface ChatRepository extends JpaRepository<ChatEntity,Long> {
}
