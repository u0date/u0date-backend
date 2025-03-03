package com.u0date.u0date_backend.mapper;

import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public static NoteDto toDto(Note note) {
        if (note == null) return null;
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .lastEditedBy(note.getLastEditedBy())
                .isSynced(note.getIsSynced())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    public Note toEntity(NoteDto dto, String accountId) {
        if (dto == null) return null;
        return Note.builder()
                .accountId(accountId) // Associate with the authenticated user
                .title(dto.getTitle())
                .content(dto.getContent())
                .lastEditedBy(dto.getLastEditedBy())
                .isSynced(dto.getIsSynced())
                .build();
    }
}
