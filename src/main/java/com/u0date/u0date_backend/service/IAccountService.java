package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.AccountDto;
import com.u0date.u0date_backend.dto.DefaultApiResponse;
import jakarta.validation.Valid;

public interface IAccountService {
    DefaultApiResponse<AccountDto> register(@Valid  AccountDto accountDto);
}
