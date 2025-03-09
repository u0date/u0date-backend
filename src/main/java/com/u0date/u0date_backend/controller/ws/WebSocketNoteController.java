package com.u0date.u0date_backend.controller.ws;

import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketNoteController {
    private final INoteService noteService;

    @MessageMapping("/note.sync")
    @SendTo("/topic/note-sync")
    public NoteDto updateNote(NoteDto noteDto, String noteId){
        try{
            log.info("note: {} {} {}", noteDto.getTitle(), noteDto.getUpdatedAt(), noteDto.getContent());
//            log.info("accountId: {}", accountPrincipal.getId());
            return noteDto;

//            return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(noteDto, noteId, accountPrincipal.getId()));
        } catch (Exception e) {
            throw new RuntimeException("===> " + e.getMessage());
        }
    }
}
