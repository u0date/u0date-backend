package com.u0date.u0date_backend.entity;

import lombok.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="notes")
public class Note extends BaseEntity{
  private String accountId;
  private String title;
  private String content;
  private String lastEditedBy;
  private Boolean isSynced;
  private LocalDateTime deletedAt;
  @Version
  private Integer version;
}
