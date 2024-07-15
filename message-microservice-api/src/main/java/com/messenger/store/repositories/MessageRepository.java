package com.messenger.store.repositories;

import com.messenger.store.entites.Chat;
import com.messenger.store.entites.Message;
import com.messenger.store.entites.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.id = :id AND m.chat = :chat AND m.author = :user")
    Optional<Message> findByIdAndChatIdAndUserId(
            @Param("id") Long idMess,
            @Param("chat") Chat chat,
            @Param("user") User user);
}
