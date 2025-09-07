package com.example.notasprivadasseguras.repo;

import com.example.notasprivadasseguras.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser_Id(Long userId);
}
