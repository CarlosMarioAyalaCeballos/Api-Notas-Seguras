package com.example.notasprivadasseguras.security;

import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.LockedException;

import java.time.Instant;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verificar si el usuario está bloqueado
        if (user.getLockUntil() != null && Instant.now().toEpochMilli() < user.getLockUntil()) {
            throw new LockedException("Usuario bloqueado por exceso de intentos fallidos. Intente más tarde.");
        }

        // 🚨 Usa getEmail() en vez de name()
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
