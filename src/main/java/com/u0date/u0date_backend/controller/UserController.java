package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.entity.User;
import com.u0date.u0date_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<User> createUser() {
        User user = User
                .builder()
                .email("baka")
                .passwordHash("kk")
                .build();
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
