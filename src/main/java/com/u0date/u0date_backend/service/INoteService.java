package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import java.util.List;

public interface INoteService {
    DefaultApiResponse<List<NoteDto>> getNotes(String accountId);
    DefaultApiResponse<NoteDto> getNote(String noteId, String accountId);
    DefaultApiResponse<NoteDto> createNote(NoteDto noteDto, String accountId);
    DefaultApiResponse<NoteDto> updateNote(NoteDto noteDto, String noteId, String accountId);
    DefaultApiResponse<NoteDto> deleteNote(String noteId, String accountId);
}
