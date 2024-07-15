package com.messenger.store.repositories;

import com.messenger.store.entites.Contact;
import com.messenger.store.entites.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.userFirst = :user OR c.userSecond = :user")
    List<Contact> findByUserFirstOrUserSecond(User user);

    @Query("SELECT c FROM Contact c WHERE (c.userFirst = :user1 AND c.userSecond = :user2)" +
            " OR (c.userFirst = :user2 AND c.userSecond = :user1)")
    Optional<Contact> findRelationByUserFirstOrUserSecond(User user1, User user2);
}
