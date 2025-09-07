package com.example.notasprivadasseguras.web;

import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.UserRepository;
import com.example.notasprivadasseguras.service.NoteService;
import com.example.notasprivadasseguras.web.dto.NoteDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes") // Actualizado para coincidir con el esquema de rutas
public class NoteController {
    private final NoteService notes;
    private final UserRepository users;

    public NoteController(NoteService notes, UserRepository users) {
        this.notes = notes;
        this.users = users;
    }

    private User me(String email) {
        return users.findByEmail(email).orElseThrow();
    }

    @PostMapping
    public Object create(@AuthenticationPrincipal UserDetails ud, @Valid @RequestBody NoteDto dto) {
        var n = notes.create(me(ud.getUsername()), dto.getTitle(), dto.getContent());
        return new Object() {
            public final Long id = n.getId();
        };
    }

    @GetMapping
    public Object list(@AuthenticationPrincipal UserDetails ud) {
        return notes.listMine(me(ud.getUsername()));
    }

    @GetMapping("/{id}")
    public Object get(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        return notes.getMine(id, me(ud.getUsername()));
    }

    @PutMapping("/{id}")
    public Object update(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id, @RequestBody NoteDto dto) {
        return notes.updateMine(id, me(ud.getUsername()), dto.getTitle(), dto.getContent());
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal UserDetails ud, @PathVariable Long id) {
        notes.deleteMine(id, me(ud.getUsername()));
    }
}
