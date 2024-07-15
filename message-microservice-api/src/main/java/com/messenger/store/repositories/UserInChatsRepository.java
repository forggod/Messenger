package com.messenger.store.repositories;

import com.messenger.store.entites.Chat;
import com.messenger.store.entites.User;
import com.messenger.store.entites.UserInChats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInChatsRepository extends CrudRepository<UserInChats, Long> {

    @Query("SELECT uic FROM UserInChats uic WHERE uic.user = :user")
    List<UserInChats> findByIdUser(@Param("user") User user);
}
