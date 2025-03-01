package com.u0date.u0date_backend.mapper;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.entity.Account;

public class AccountMapper {
    public static AccountDto toDTO(Account account){
        if (account == null) return null;
        return AccountDto.builder()
                .username(account.getUsername())
                .email(account.getEmail())
                .password(account.getPassword())
                .build();
    }

    public static Account toEntity(AccountDto accountDto){
        if (accountDto == null) return null;
        return Account.builder()
                .username(accountDto.getUsername())
                .email(accountDto.getEmail())
                .password(accountDto.getPassword())
                .build();
    }
}
