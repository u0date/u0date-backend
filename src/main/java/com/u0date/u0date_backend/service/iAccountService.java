package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.dto.DefaultApiResponse;

public interface iAccountService {
    DefaultApiResponse<AccountDto> register(AccountDto accountDto);
}
