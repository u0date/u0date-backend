package com.u0date.u0date_backend.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

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
  @Builder.Default
  private Map<String, String> lastRefreshTokenMap = new HashMap<>();

  public void updateRefreshToken(String deviceId, String tokenId) {
    lastRefreshTokenMap.put(deviceId, tokenId);
  }

  public boolean isValidRefreshToken(String deviceId, String tokenId) {
    return lastRefreshTokenMap.containsKey(deviceId) && lastRefreshTokenMap.get(deviceId).equals(tokenId);
  }

  public void removeRefreshToken(String deviceId) {
    lastRefreshTokenMap.remove(deviceId);
  }
}
