package com.example.notasprivadasseguras.web.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @Email @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{10,}$",
            message = "La contraseña debe tener ≥10 caracteres, 1 mayúscula, 1 minúscula y 1 dígito")
    private String password;

    private boolean admin;

    // getters/setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
