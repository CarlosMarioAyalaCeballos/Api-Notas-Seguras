package com.example.notasprivadasseguras.web;

import com.example.notasprivadasseguras.domain.User;
import com.example.notasprivadasseguras.repo.UserRepository;
import com.example.notasprivadasseguras.security.AppUserDetailsService;
import com.example.notasprivadasseguras.security.JwtUtil;
import com.example.notasprivadasseguras.service.UserService;
import com.example.notasprivadasseguras.web.dto.LoginRequest;
import com.example.notasprivadasseguras.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final UserRepository users;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    public AuthController(UserService userService, UserRepository users,
                        AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                        AppUserDetailsService userDetailsService) {
        this.userService = userService;
        this.users = users;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String accessToken = jwtUtil.generateAccessToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            response.addHeader(HttpHeaders.SET_COOKIE,
                jwtUtil.generateRefreshTokenCookie(refreshToken).toString());

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        try {
            String email = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,
                        jwtUtil.generateRefreshTokenCookie(newRefreshToken).toString())
                    .body(new Object() {
                        public final String token = newAccessToken;
                        public final String type = "Bearer";
                    });
            }

            return ResponseEntity.status(401).body(new Object() {
                public final String error = "Refresh token inválido";
            });
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new Object() {
                public final String error = "Error al refrescar token";
            });
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE,
            jwtUtil.getCleanRefreshTokenCookie().toString());

        return ResponseEntity.ok().body(new Object() {
            public final String message = "Sesión cerrada exitosamente";
        });
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            User user = new User();
            user.setEmail(req.getEmail());
            user.setPassword(req.getPassword());
            user.setAdmin(req.isAdmin());
            User u = userService.register(user);

            return ResponseEntity.ok().body(new Object() {
                public final Long id = u.getId();
                public final String email = u.getEmail();
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Object() {
                public final String error = e.getMessage();
            });
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        User u = users.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok().body(new Object() {
            public final Long id = u.getId();
            public final String email = u.getEmail();
            public final Object roles = u.getRoles();
        });
    }
}
