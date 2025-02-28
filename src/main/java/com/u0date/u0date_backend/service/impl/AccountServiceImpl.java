package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.mapper.AccountMapper;
import com.u0date.u0date_backend.repository.AccountRepository;
import com.u0date.u0date_backend.service.iAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements iAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DefaultApiResponse<AccountDto> register(AccountDto accountDto) {
        DefaultApiResponse<AccountDto> response = new DefaultApiResponse<>();

        verifyRecord(accountDto);
        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account savedAccount  = accountRepository.save(AccountMapper.toEntity(accountDto));
        savedAccount.setPassword(null); // prevent password from returning to user

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setStatusMessage("Successfully Created Account");
        response.setData(AccountMapper.toDTO(savedAccount));
        return response;
    }

    private void verifyRecord(AccountDto accountDto){
        if (accountRepository.existsByEmail(accountDto.getEmail())
                || accountRepository.existsByUsername(accountDto.getUsername()))
            throw new RuntimeException("Username or Email Address has already been used");
    }
}
