package com.example.notasprivadasseguras.config;

import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.UserRepository;
import com.example.notasprivadasseguras.security.LockedException;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class AuthEvents {
    private final UserRepository users;
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_MINUTES = 15;

    public AuthEvents(UserRepository users) {
        this.users = users;
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();

        users.findByEmail(email).ifPresent(user -> {
            // Si el usuario está bloqueado y no ha pasado el tiempo
            if (user.getLockUntil() != null && Instant.now().toEpochMilli() < user.getLockUntil()) {
                throw new LockedException("Usuario bloqueado. Intente de nuevo en " + LOCK_TIME_MINUTES + " minutos");
            }

            // Si el bloqueo expiró, reiniciar intentos
            if (user.getLockUntil() != null && Instant.now().toEpochMilli() >= user.getLockUntil()) {
                user.setFailedAttempts(0);
                user.setLockUntil(null);
            }

            // Incrementar intentos fallidos
            user.setFailedAttempts(user.getFailedAttempts() + 1);

            // Bloquear si excede intentos
            if (user.getFailedAttempts() >= MAX_ATTEMPTS) {
                user.setLockUntil(Instant.now().plusMillis(LOCK_TIME_MINUTES * 60 * 1000).toEpochMilli());
            }

            users.save(user);

            if (user.getFailedAttempts() >= MAX_ATTEMPTS) {
                throw new LockedException("Usuario bloqueado por " + LOCK_TIME_MINUTES + " minutos debido a múltiples intentos fallidos");
            }
        });
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        users.findByEmail(email).ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setLockUntil(null);
            users.save(user);
        });
    }
}
