package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;

    @PostMapping("/create")
    public ResponseEntity<Account> createUser() {
        Account account = Account
                .builder()
                .email("exameple@email.com")
                .passwordHash("hashedpassword")
                .build();
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }
}
