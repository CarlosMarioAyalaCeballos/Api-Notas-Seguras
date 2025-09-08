package com.example.notasprivadasseguras.service;

import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        // Verificar si el email ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        // Codificar contraseña y asignar roles
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER")); // Agregamos el prefijo ROLE_
        user.setFailedAttempts(0);
        user.setLockUntil(null);

        return userRepository.save(user);
    }
}
