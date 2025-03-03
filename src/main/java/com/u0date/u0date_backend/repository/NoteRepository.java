package com.u0date.u0date_backend.repository;

import com.u0date.u0date_backend.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByAccountIdAndDeletedAtIsNull(String userId);
}
