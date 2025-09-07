package com.example.notasprivadasseguras.repo;

import com.example.notasprivadasseguras.domain.Note;
import com.example.notasprivadasseguras.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwner(User owner);
    List<Note> findByOwnerId(Long ownerId);
    Optional<Note> findByIdAndOwnerId(Long id, Long ownerId);
}
