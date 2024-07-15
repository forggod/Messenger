package com.messenger.authorization.microservice.api.store.entites;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "role_name",
            length = 40, unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
