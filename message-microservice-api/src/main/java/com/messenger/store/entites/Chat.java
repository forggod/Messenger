package com.messenger.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_creator")
    private User creator;

    @OneToMany(mappedBy = "chat")
    private List<UserInChats> usersInChats = new ArrayList<>();

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Chat(){

    }

    public Chat(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.createdAt = new Date().toInstant();
        this.isDeleted = false;
    }
}
