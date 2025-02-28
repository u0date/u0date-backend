package com.u0date.u0date_backend.repository;

import com.u0date.u0date_backend.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> { }
