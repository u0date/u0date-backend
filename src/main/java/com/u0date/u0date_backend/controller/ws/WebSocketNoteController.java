package com.u0date.u0date_backend.controller.ws;

import com.u0date.u0date_backend.dto.NoteDto;
import com.u0date.u0date_backend.entity.AccountPrincipal;
import com.u0date.u0date_backend.service.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketNoteController {
    private final INoteService noteService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/note.sync")
    public void syncNote(@Payload NoteDto noteDto, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes){
        try{
            AccountPrincipal accountPrincipal = (AccountPrincipal) sessionAttributes.get("principal");
            if (accountPrincipal != null) {
                String accountId = accountPrincipal.getId();
                messagingTemplate.convertAndSend("/topic/note-sync/" + accountId, noteService.updateNote(noteDto, noteDto.getId(), accountId));
            } else {
                log.warn("No principal found in session attributes");
            }
        } catch (Exception e) {
            log.error("WebSocket Note Updated Error:\nMessage: {}\nAccountID: {}\nNoteID: {}", e.getMessage(), (AccountPrincipal) sessionAttributes.get("principal"), noteDto.getId());
        }
    }
}
