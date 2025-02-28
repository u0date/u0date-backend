package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.entity.Note;
import com.u0date.u0date_backend.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
  private final NoteRepository noteRepository;
  @PostMapping("/create")
  public ResponseEntity<Note> createNote() {
    Note note = Note.builder()
      .userId("user123")
      .title("First Note")
      .content("This is my first note.")
      .lastEditedBy("user123")
      .isSynced(true)
      .build();
    Note savedNote = noteRepository.save(note);
    return ResponseEntity.ok(savedNote);
  }
}
