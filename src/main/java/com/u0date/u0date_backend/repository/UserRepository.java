package com.u0date.u0date_backend.repository;

import com.u0date.u0date_backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> { }
