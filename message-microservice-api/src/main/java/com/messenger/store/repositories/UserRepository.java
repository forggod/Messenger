package com.messenger.store.repositories;

import com.messenger.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(@Param("email") String email);

    Optional<User> findByPhone(String phone);

    Boolean existsByPhone(@Param("phone") String phone);
}
