package com.u0date.u0date_backend.service;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import java.util.List;

public interface INoteService {
    DefaultApiResponse<List<NoteDto>> getAllNotes(String accountId);
}
