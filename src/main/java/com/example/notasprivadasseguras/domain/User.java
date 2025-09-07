package com.example.notasprivadasseguras.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private boolean admin;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    // 🔹 para lockout
    private int failedAttempts;
    private Long lockUntil;

    // --- Getters y Setters ---
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }

    public Set<String> getRoles() { return roles; }

    public void setRoles(Set<String> roles) { this.roles = roles; }

    public int getFailedAttempts() { return failedAttempts; }

    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public Long getLockUntil() { return lockUntil; }

    public void setLockUntil(Long lockUntil) { this.lockUntil = lockUntil; }
}
