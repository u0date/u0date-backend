package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.AccountPrincipal;
import com.u0date.u0date_backend.service.INoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final INoteService noteService;

    @GetMapping
    public ResponseEntity<DefaultApiResponse<List<NoteDto>>> getNotes(@AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNotes(accountPrincipal.getId()));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<DefaultApiResponse<NoteDto>> getNote(@PathVariable("noteId") String noteId, @AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNote(noteId, accountPrincipal.getId()));
    }

    @PostMapping
    public ResponseEntity<DefaultApiResponse<NoteDto>> createNote(@Valid @RequestBody NoteDto noteDto, @AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.createNote(noteDto, accountPrincipal.getId()));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable("noteId") String noteId, @RequestBody NoteDto noteDto, @AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(noteDto, noteId, accountPrincipal.getId()));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<DefaultApiResponse<NoteDto>> deleteNote(@PathVariable("noteId") String noteId, @AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.deleteNote(noteId, accountPrincipal.getId()));
    }

}
