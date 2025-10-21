package com.example.notasprivadasseguras.web;

import com.example.notasprivadasseguras.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                    @RequestParam String password,
                                    HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String accessToken = jwtUtil.generateAccessToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            response.addHeader(HttpHeaders.SET_COOKIE,
                jwtUtil.generateRefreshTokenCookie(refreshToken).toString());
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            return ResponseEntity.ok().body(new Object() {
                public final String token = accessToken;
                public final String type = "Bearer";
                public final String email = userDetails.getUsername();
            });
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new Object() {
                public final String error = "Credenciales inválidas";
            });
        }
    }
}

