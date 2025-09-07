package com.example.notasprivadasseguras.security;

import org.springframework.security.core.AuthenticationException;

public class LockedException extends AuthenticationException {
    public LockedException(String msg) {
        super(msg);
    }
}
