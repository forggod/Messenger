package com.messenger.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "users_in_chats")
public class UserInChats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_chat",
            nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_user",
            nullable = false)
    private User user;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public UserInChats(){

    }

    public UserInChats(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
        this.createdAt = new Date().toInstant();
        this.isDeleted = false;
    }
}
