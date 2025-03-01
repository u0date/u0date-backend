package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.entity.Account;
import com.u0date.u0date_backend.mapper.AccountMapper;
import com.u0date.u0date_backend.repository.AccountRepository;
import com.u0date.u0date_backend.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DefaultApiResponse<AccountDto> register(AccountDto accountDto) {
        verifyAccount(accountDto);

        DefaultApiResponse<AccountDto> response = new DefaultApiResponse<>();
        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account savedAccount  = accountRepository.save(AccountMapper.toEntity(accountDto));
        savedAccount.setPassword(null); // prevent password from returning to user
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setStatusMessage("Successfully Created Account");
        response.setData(AccountMapper.toDTO(savedAccount));
        return response;
    }

    private void verifyAccount(AccountDto accountDto){
        if (accountRepository.existsByEmail(accountDto.getEmail())
                || accountRepository.existsByUsername(accountDto.getUsername()))
            throw new BadCredentialsException("Username or Email Address has already been used");
    }
}
