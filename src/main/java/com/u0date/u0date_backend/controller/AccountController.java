package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.entity.AccountPrincipal;
import com.u0date.u0date_backend.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<DefaultApiResponse<AccountDto>> register(@Valid @RequestBody AccountDto accountDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.register(accountDto));
    }

    @GetMapping("/accountId")
    public ResponseEntity<DefaultApiResponse<Map<String, String>>> getAccountId(@AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(
                new DefaultApiResponse<>(HttpStatus.OK.value(), "accountId", Map.of("accountId",accountPrincipal.getId()))
        );
    }
}
