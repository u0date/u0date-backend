package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.service.iAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AccountController {
    private final iAccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.register(account));
    }
}
