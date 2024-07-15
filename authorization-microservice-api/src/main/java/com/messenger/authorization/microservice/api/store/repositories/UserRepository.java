package com.messenger.authorization.microservice.api.store.repositories;

import com.messenger.authorization.microservice.api.store.entites.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    Boolean existsByPhone(String phone);
}
