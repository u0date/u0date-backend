package com.u0date.u0date_backend.controller.ws;

import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketNoteController {
    private final INoteService noteService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/note.sync")
    public void updateNote(@Payload NoteDto noteDto,
                           @Header("accountId") String accountId){
        try{
            messagingTemplate.convertAndSend("/topic/note-sync/" + accountId, noteDto);
        } catch (Exception e) {
            throw new RuntimeException("===> " + e.getMessage());
        }
    }
}
