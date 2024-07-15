package com.messenger.authorization.microservice.api.store.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    @Column(name = "roles")
    private Collection<Role> roles = new HashSet<>();

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public User(String username, String email, String phone, String password) {
        super();
        Clock clock = Clock.systemUTC();
        Instant instant = clock.instant();
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.lastLogin = instant;
        this.createdAt = instant;
        this.isDeleted = false;
    }

    public User() {

    }

    public String toString() {
        return "User {" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "email = " + email + ", " +
                "phone = " + phone + ", " +
                "password = " + password + ", " +
                "role = " + getRoles() + ", " +
                "last_login = " + lastLogin + ", " +
                "created_at = " + createdAt + "}";
    }
}
