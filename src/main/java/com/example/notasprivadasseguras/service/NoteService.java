package com.example.notasprivadasseguras.service;

import com.example.notasprivadasseguras.domain.Note;
import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> findByOwnerId(Long ownerId) {
        return noteRepository.findByOwnerId(ownerId);
    }

    public Optional<Note> findByIdAndOwnerId(Long id, Long ownerId) {
        return noteRepository.findByIdAndOwnerId(id, ownerId);
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note create(User owner, String title, String content) {
        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public List<Note> listMine(User owner) {
        return noteRepository.findByOwner(owner);
    }

    public Note getMine(Long id, User owner) {
        return noteRepository.findByIdAndOwnerId(id, owner.getId()).orElse(null);
    }

    public Note updateMine(Long id, User owner, String title, String content) {
        Optional<Note> optNote = noteRepository.findByIdAndOwnerId(id, owner.getId());
        if (optNote.isPresent()) {
            Note note = optNote.get();
            note.setTitle(title);
            note.setContent(content);
            return noteRepository.save(note);
        }
        return null;
    }

    public void deleteMine(Long id, User owner) {
        Optional<Note> optNote = noteRepository.findByIdAndOwnerId(id, owner.getId());
        optNote.ifPresent(note -> noteRepository.deleteById(note.getId()));
    }
}
