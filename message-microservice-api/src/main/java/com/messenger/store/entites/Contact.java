package com.messenger.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_userFirst")
    private User userFirst;

    @ManyToOne
    @JoinColumn(name = "id_userSecond")
    private User userSecond;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Contact(){

    }

    public Contact(User userFirst, User userSecond) {
        this.userFirst = userFirst;
        this.userSecond = userSecond;
        this.createdAt = new Date().toInstant();
        this.isDeleted = false;
    }
}
