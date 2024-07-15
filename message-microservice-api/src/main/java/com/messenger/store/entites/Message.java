package com.messenger.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_chat")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User author;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_seen")
    private Boolean isSeen;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Message(){

    }

    public Message(Chat chat, User author, String message) {
        this.chat = chat;
        this.author = author;
        this.message = message;
        this.createdAt = new Date().toInstant();
        this.updatedAt = new Date().toInstant();
        this.isSeen = false;
        this.isDeleted = false;
    }
}
