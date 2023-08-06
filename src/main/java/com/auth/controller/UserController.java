package com.auth.controller;

import com.auth.entity.User;
import com.auth.security.AuthRequest;
import com.auth.security.AuthResponse;
import com.auth.security.JWTTokenProvider;
import com.auth.security.JwtTokenBlacklist;
import com.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenBlacklist jwtTokenBlacklist;

    @Autowired
    public UserController(JWTTokenProvider jwtTokenProvider, UserService userService, BCryptPasswordEncoder passwordEncoder, JwtTokenBlacklist jwtTokenBlacklist) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenBlacklist = jwtTokenBlacklist;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            String username = authRequest.username();
            String password = authRequest.password();

            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty() || passwordEncoder.matches(password, user.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильное имя/пароль");
            }

            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильное имя/пароль");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest registrationRequest) {
        if (userService.findByUsername(registrationRequest.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Имя занятно");
        }

        User user = new User(registrationRequest.username(), passwordEncoder.encode(registrationRequest.password()));
        userService.saveUser(user);

        return ResponseEntity.ok("Зарегистрировали");
    }
}

