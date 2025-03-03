package com.u0date.u0date_backend.service.impl;

import com.u0date.u0date_backend.dto.DefaultApiResponse;
import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.Note;
import com.u0date.u0date_backend.mapper.NoteMapper;
import com.u0date.u0date_backend.repository.NoteRepository;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements INoteService {
    private final NoteRepository noteRepository;

    @Override
    public DefaultApiResponse<List<NoteDto>> getAllNotes(String accountId) {
        DefaultApiResponse<List<NoteDto>> response = new DefaultApiResponse<>();
        List<Note> notes = noteRepository.findByAccountIdAndDeletedAtIsNull(accountId);
        List<NoteDto> noteDtoList = notes.stream()
                .map(NoteMapper::toDto)
                .toList();

        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("All Notes");
        response.setData(noteDtoList);
        return response;
    }
}
