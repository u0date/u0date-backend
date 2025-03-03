package com.u0date.u0date_backend.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="accounts")
public class Account extends BaseEntity{
  @Indexed(unique = true)
  private String email;
  private String username;
  private String password;
  private String lastRefreshTokenId;
}
