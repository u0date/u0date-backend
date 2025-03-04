package com.u0date.u0date_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDto {
    private String id;
    @NotNull(message = "title cannot be null or empty")
    private String title;
    @NotNull(message = "content cannot be null or empty")
    private String content;
    private LocalDateTime updatedAt;
    private String lastEditedBy;
    private Boolean isSynced;
}
