package com.example.notasprivadasseguras.web;

import com.example.notasprivadasseguras.repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/admin")
public class AdminController {
    private final UserRepository users; public AdminController(UserRepository users){this.users=users;}
    @GetMapping("/users") @PreAuthorize("hasRole('ADMIN')")
    public Object allUsers(){ return users.findAll(); }
}
