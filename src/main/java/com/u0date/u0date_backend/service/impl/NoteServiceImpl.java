package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.Note;
import com.u0date.u0date_backend.exception.ResourceNotFound;
import com.u0date.u0date_backend.mapper.NoteMapper;
import com.u0date.u0date_backend.repository.NoteRepository;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements INoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public DefaultApiResponse<List<NoteDto>> getNotes(String accountId) {
        List<Note> notes = noteRepository.findByAccountIdAndDeletedAtIsNull(accountId);
        List<NoteDto> noteDtoList = notes.stream()
                .map(noteMapper::toDto)
                .toList();
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Retrieved all notes", noteDtoList);
    }

    @Override
    public DefaultApiResponse<NoteDto> getNote(String noteId, String accountId) {
        Note note = noteRepository.findByIdAndAccountIdAndDeletedAtIsNull(noteId, accountId)
                .orElseThrow(() -> {
                    log.warn("[Note Retrieval] Note not found - noteId: {}, accountId: {}", noteId, accountId);
                    return new ResourceNotFound("Note not found for the given account");
                });
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Retrieved note", noteMapper.toDto(note));
    }

    @Override
    public DefaultApiResponse<NoteDto> createNote(NoteDto noteDto, String accountId) {
        Note note = noteMapper.toEntity(noteDto, accountId);
        Note savedNote = noteRepository.save(note);
        return new DefaultApiResponse<>(HttpStatus.CREATED.value(), "Note created successfully", noteMapper.toDto(savedNote));
    }

    @Override
    public DefaultApiResponse<NoteDto> updateNote(NoteDto noteDto, String noteId, String accountId) {
        Note existingNote = noteRepository.findByIdAndAccountIdAndDeletedAtIsNull(noteId, accountId)
                .orElseThrow(() -> {
                    log.warn("[Note Retrieval] Note not found - noteId: {}, accountId: {}", noteId, accountId);
                    return new ResourceNotFound("Note not found for the given account");
                });

        if (existingNote.getUpdatedAt().isAfter(noteDto.getUpdatedAt()))
            return null;

        if (noteDto.getTitle() != null && !noteDto.getTitle().isEmpty())
            existingNote.setTitle(noteDto.getTitle());

        existingNote.setContent(noteDto.getContent());
        existingNote.setLastEditedBy(accountId);
        existingNote.setIsSynced(false);
        Note updateNote = noteRepository.save(existingNote);
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Note updated successfully", noteMapper.toDto(updateNote));
    }

    @Override
    public DefaultApiResponse<NoteDto> deleteNote(String noteId, String accountId) {
        Note note = noteRepository.findByIdAndAccountIdAndDeletedAtIsNull(noteId, accountId)
                .orElseThrow(() -> {
                    log.warn("[Note Retrieval] Note not found - noteId: {}, accountId: {}", noteId, accountId);
                    return new ResourceNotFound("Note not found for the given account");
                });

        note.setDeletedAt(LocalDateTime.now());
        noteRepository.save(note);
        return new DefaultApiResponse<>(HttpStatus.OK.value(), "Note deleted successfully", null);
    }
}
