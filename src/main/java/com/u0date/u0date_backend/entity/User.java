package com.u0date.u0date_backend.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="users")
public class User extends BaseEntity{
  private String email;
  private String passwordHash;
}
