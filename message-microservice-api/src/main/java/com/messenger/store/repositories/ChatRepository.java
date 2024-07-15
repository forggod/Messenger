package com.messenger.store.repositories;

import com.messenger.store.entites.Chat;
import com.messenger.store.entites.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {
    Optional<Chat> findByIdAndCreator(Long id, User user);
}
