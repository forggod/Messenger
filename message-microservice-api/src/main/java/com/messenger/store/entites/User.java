package com.messenger.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users_message")
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @OneToMany(mappedBy = "id")
    private List<UserInChats> chats = new ArrayList<>();

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_online")
    private Boolean isOnline;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public User(){

    }

    public User(Long id, String username, String email, String phone, Instant lastLogin,
                Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.isOnline = true;
        this.isDeleted = false;
    }
}
