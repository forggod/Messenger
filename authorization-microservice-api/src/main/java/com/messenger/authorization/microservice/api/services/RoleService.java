package com.messenger.authorization.microservice.api.services;

import com.messenger.authorization.microservice.api.store.entites.Role;
import com.messenger.authorization.microservice.api.store.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        Optional <Role> roleUser= roleRepository.findByName("ROLE_USER");
        return roleUser.orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
    }
}
