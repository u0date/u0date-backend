package com.u0date.u0date_backend.repository;

import com.u0date.u0date_backend.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> { }
