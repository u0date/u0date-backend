package com.u0date.u0date_backend.controller;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.AccountPrincipal;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final INoteService noteService;

    @GetMapping
    public ResponseEntity<DefaultApiResponse<List<NoteDto>>> getNotes(@AuthenticationPrincipal AccountPrincipal accountPrincipal){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getAllNotes(accountPrincipal.getId()));
    }
}
